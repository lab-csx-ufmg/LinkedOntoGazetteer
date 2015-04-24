import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import com.thinkaurelius.titan.core.attribute.Geoshape;


public class CopyOfGeoCompare {
	
	private static Logger LOGGER = Logger.getLogger(CopyOfGeoCompare.class);
	
	private static double extractDouble(String attr, String regex
			) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(attr);
		
		String matched = null;
		
		if(m.find()) {
			matched = m.group();
		}
		
		return Double.valueOf(matched.replaceAll("[<>]", ""));
	}
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		PropertyConfigurator.configure("log4jfilter.properties");
		String filePath = "/media/My Passport/linkedData/geonames/all-geonames-rdf.txt";
		String geoIdsfpath = "/media/My Passport/linkedData/allnewlinkssortuniq";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		BufferedReader geoIdsreader = new BufferedReader(new FileReader(new File(geoIdsfpath)));
		String line;
		
		FileWriter fwgeocoord = new FileWriter("/media/My Passport/linkedData/geonames/lat_long_geonames");
		
		Map<String, Geoshape> geoIds = new HashMap<String, Geoshape>();
		Map<String, Geoshape> dbIds = new HashMap<String, Geoshape>();
		
		while((line = geoIdsreader.readLine()) != null) {
			geoIds.put(line.trim(), null);
		} 
		System.out.println("Geonames Ids loaded");
		geoIdsreader.close();

		Pattern p = Pattern.compile("[0-9]+");
		
		while((line = reader.readLine()) != null) {
			Matcher matcher = p.matcher(line);
			matcher.find();
			String geonamesId = matcher.group();
			
			String xml = reader.readLine();
			
			Matcher latMatcher = Pattern.compile("<wgs84_pos:lat>.+</wgs84_pos:lat>").matcher(xml);
			Matcher longMatcher = Pattern.compile("<wgs84_pos:long>.+</wgs84_pos:long>").matcher(xml);
			
			if(latMatcher.find() && longMatcher.find()) {
				fwgeocoord.write(geonamesId.trim() + "\t"+ latMatcher.group() + "\t" + longMatcher.group() + "\n");
				if(!geoIds.containsKey(geonamesId)) {
					continue;
				}
				geoIds.put(geonamesId.trim(), Geoshape.point(CopyOfGeoCompare.extractDouble(latMatcher.group(), ">.*<"), CopyOfGeoCompare.extractDouble(longMatcher.group(), ">.*<")));
			}
		}
		
		fwgeocoord.close();
		System.out.println("Geonames points loaded");
		reader.close();
		
		reader = new BufferedReader(new FileReader("/media/My Passport/linkedData/dbpedia/lat_long2"));
		
		while((line = reader.readLine()) != null) {
			String[] split = line.split("\t\t");
			String[] coord = split[1].split(",");
			
			dbIds.put(split[0].trim(), Geoshape.point(Double.valueOf(coord[0]), Double.valueOf(coord[1])));
		}
		
		reader.close();

		System.out.println("DBPedia points loaded");
		
		reader = new BufferedReader(new FileReader("/media/My Passport/linkedData/dbgeo_semanticalMatchsortuniq"));
		
		while((line = reader.readLine()) != null) {
			String[] split = line.split(" -> ");
			
			String geoid = split[0].trim();
			String dbid = split[1].trim();
			
			Geoshape geop = geoIds.get(geoid);
			Geoshape dbp = dbIds.get(dbid);

			if(geop != null && dbp != null) {
				CopyOfGeoCompare.LOGGER.info(geoid + "\t" + dbid + "\t" + geop.getPoint().distance(dbp.getPoint()));
			} else if(geop == null) {
				CopyOfGeoCompare.LOGGER.warn(geoid + " not found.");
			} else if(dbp == null) {
				CopyOfGeoCompare.LOGGER.warn(dbid + " not found.");
			}
			
		}
		
		System.out.println("Finished");
	}
	
}
