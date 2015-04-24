package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mestrado.linkedDataGraph.business.DataFusionHandler;
import mestrado.linkedDataGraph.business.NameBO;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class LoadGeonamesDataApp {

	private static final Pattern idPattern = Pattern.compile("[0-9]+");
	
	private static final Logger LOGGER = Logger.getLogger(LoadGeonamesDataApp.class);
	
	public static void main(String[] args) throws IOException {
	
		PropertyConfigurator.configure("log4jfilter.properties");
		
		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
		
    	BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandra");
		config.setProperty("storage.hostname","127.0.0.1");
    	
    	FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
    	PlaceBO placeBO = new PlaceBO(graph);
    	NameBO nameBO = new NameBO(graph);
    	DataFusionHandler dataFusionHandler = new DataFusionHandler(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("geonames.fpath")));
    	
    	String line;

    	double count = 0;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;
    	
    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split("\t");
    		String subjectId = LoadGeonamesDataApp.getId(split[0]);
    		
    		if(line.contains("gn:name")) {
    			LOGGER.debug(line);
    			Name name = nameBO.createNameIfNotExists(split[2].trim());
    			Place place = placeBO.createGeoNamesPlaceIfNotExists(subjectId);
    			placeBO.addPlaceName(place, name);
    			
    		} else if(line.contains("gn:alternateName")) {
    			LOGGER.debug(line);
            	for(String namestr : split[2].split(",")) {
            		
            		
        			Name name = nameBO.createNameIfNotExists(namestr.trim());
        			Place place = placeBO.createGeoNamesPlaceIfNotExists(subjectId);
        			placeBO.addPlaceName(place, name);
            	}
            	
            } else if(line.contains("gn:parentFeature") || line.contains("gn:parentADM")) {
            	LOGGER.debug(line);

            	String objectId = LoadGeonamesDataApp.getId(split[2]);

            	Place place = placeBO.createGeoNamesPlaceIfNotExists(subjectId);
            	Place containedBy = placeBO.createGeoNamesPlaceIfNotExists(objectId);
            	
            	placeBO.addPlaceContainedBy(place, containedBy);
            	
            	
            } else if(line.contains("gn:wikipediaArticle")) {
            	LOGGER.debug(line);
            	
            	for(String url : split[2].split(",")) {
            		if(url.startsWith("http://en.wikipedia.org/wiki/")) {
            			dataFusionHandler.handleDBGeoFusion(url.substring("http://en.wikipedia.org/wiki/".length()), subjectId);
            			break;
            		}
            	}
            	
            } else if(line.contains("gn:parentCountry")) {
            	String objectId = LoadGeonamesDataApp.getId(split[2]);
            	
            	Place place = placeBO.createGeoNamesPlaceIfNotExists(subjectId);
            	Place country = placeBO.createGeoNamesPlaceIfNotExists(objectId);
            	
            	placeBO.addPlaceCountry(place, country);
            }
    		
    		if(count++ % 1000 == 0) {
    			graph.getBaseGraph().commit();

    			endTime = System.currentTimeMillis();
    			System.out.print(" - Lap time: " + (endTime - lastTime)/1000.0 + " s");
    			System.out.println(" - Total time: " + (endTime - startTime)/1000.0 + " s");
    			lastTime = endTime;

    		}
    		
    	}

    	graph.shutdown();
    	reader.close();
    	
	}
	
	public static String getId(String txt) {
		Matcher m = idPattern.matcher(txt);
    	m.find();
    	
    	return m.group();
	}
	
}
