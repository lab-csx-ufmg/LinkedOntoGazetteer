import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class GeoCoordExtract {
	private static double extractDouble(String attr, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(attr);
		
		String matched = null;
		
		if(m.find()) {
			matched = m.group();
		}
		
		return Double.valueOf(matched.replaceAll("[<>]", ""));
	}
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		String filePath = "/media/My Passport/linkedData/geonames/all-geonames-rdf.txt";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		String line;
		
		FileWriter fwgeocoord = new FileWriter("/media/My Passport/linkedData/geonames/lat_long_geonames_full");
		
		Pattern p = Pattern.compile("[0-9]+");
		
		while((line = reader.readLine()) != null) {
			Matcher matcher = p.matcher(line);
			matcher.find();
			String geonamesId = matcher.group();
 			String xml = reader.readLine();
			
			Matcher latMatcher = Pattern.compile("<wgs84_pos:lat>.+</wgs84_pos:lat>").matcher(xml);
			Matcher longMatcher = Pattern.compile("<wgs84_pos:long>.+</wgs84_pos:long>").matcher(xml);
			
			if(latMatcher.find() && longMatcher.find()) {
				double lat = GeoCoordExtract.extractDouble(latMatcher.group(), ">.*<");
				double log = GeoCoordExtract.extractDouble(longMatcher.group(), ">.*<");
				
				fwgeocoord.write(geonamesId.trim() + "\t"+ lat + "\t" + log + "\n");
			}
		}
		
		fwgeocoord.close();
		reader.close();
	}

	
	
	
}
