import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import mestrado.linkedDataGraph.LinkedOntoGztSchema;
import mestrado.linkedDataGraph.business.LDIterables;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.business.PlaceEqualityCalculator;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.frames.FramedGraph;


public class GeoCompare {
	
	private static Logger LOGGER = Logger.getLogger(GeoCompare.class);
	
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
		PropertyConfigurator.configure("log4jfilter.properties");
		String filePath = "/media/My Passport/linkedData/geonames/all-geonames-rdf.txt";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		String line;
		
		BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandrathrift");
		config.setProperty("storage.hostname","192.168.0.119");
		config.setProperty("storage.index.search.backend","elasticsearch");
		config.setProperty("storage.index.search.directory","../db/es");
		config.setProperty("storage.index.search.client-only",false);
		config.setProperty("storage.index.search.local-mode",true);


		FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
		PlaceBO placeBO = new PlaceBO(graph);
		
		
		FileWriter fwgeocoord = new FileWriter("/media/My Passport/linkedData/geonames/lat_long_geonames");
		
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
				Geoshape point = Geoshape.point(GeoCompare.extractDouble(latMatcher.group(), ">.*<"), GeoCompare.extractDouble(longMatcher.group(), ">.*<"));
				
				Place place = placeBO.retrievePlaceByGeonamesId(geonamesId.trim());
				if(place == null) {
					GeoCompare.LOGGER.error("Nao existe: \t" + geonamesId);
					continue;
				}
				
				place.setGeonamesPoint(point);
				graph.getBaseGraph().commit();
				
				Iterable<Place> ambiguosDbPediaPlaces = place.getAmbiguosDbPediaPlaces();
				
				Iterable<Place> countries = place.getCountries();
				Iterable<Place> containedBy = place.getContainedBy();
				Geoshape bhpoint = place.getGeonamesPoint() != null ? place.getGeonamesPoint() : place.getDBPediaPoint();
				
				for(Place candidate : ambiguosDbPediaPlaces) {
					Iterable<Place> candidateCountries = candidate.getCountries();
					Iterable<Place> candidateContainedBy = candidate.getContainedBy();
		
					double equalsProbability = PlaceEqualityCalculator.calculateEquality(!LDIterables.disjoint(candidateCountries, countries), LDIterables.countEqualsElements(containedBy, candidateContainedBy), place.getGeonamesPoint(), candidate.getDBPediaPoint());
					
					if(equalsProbability > 0.0) {
						System.out.println("PlaceEqualityCalculator:\t" + equalsProbability + "\t" + geonamesId + "\t" + place + "\t" + candidate.getDbpediaId() + "\t" + candidate + "\t" + bhpoint + "\t" + candidate.getDBPediaPoint());
					}
					
				}
				
				GeoCompare.LOGGER.info(geonamesId + "\t" + place.toString() + "\t" + point);
				
			}
		}
		
		fwgeocoord.close();
		System.out.println("Geonames points loaded");
		reader.close();
		
		graph.shutdown();
	}
	
}
