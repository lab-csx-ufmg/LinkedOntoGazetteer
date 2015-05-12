package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.business.NonPlaceBO;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.edge.Place2NPlaceRel;
import mestrado.linkedDataGraph.vertex.NonPlace;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class LoadDBPediaNonPlacesRelationships {

	private static Logger LOGGER = Logger.getLogger(LoadDBPediaNonPlacesRelationships.class);
	
	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4jfilter.properties");
		
		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
		
		BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandrathrift");
		config.setProperty("storage.hostname",System.getProperty("storage.hostname"));
		config.setProperty("storage.index.search.backend","elasticsearch");
		config.setProperty("storage.index.search.directory","../db/es");
		config.setProperty("storage.index.search.client-only",false);
		config.setProperty("storage.index.search.local-mode",true);
    	
    	FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
    	NonPlaceBO nonPlaceBO = new NonPlaceBO(graph);
    	PlaceBO placeBO = new PlaceBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("dbpedia.nonPlace.fpath")));
    	
    	String line;
    	double count = 1;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;

    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split(" ");
    		String nonplaceId = split[0].substring("http://dbpedia.org/resource/".length());
    		String placeId = split[2].substring("http://dbpedia.org/resource/".length());
    		
    		String predicate = split[1];
    		double ms0 = System.currentTimeMillis();
    		Place place = placeBO.retrievePlaceByDBPediaId(placeId);
    		double ms1 = System.currentTimeMillis();
    		
    		LoadDBPediaNonPlacesRelationships.LOGGER.info("@TIME@RETRIVING PLACE:\t" + (ms1-ms0) + "ms");
    		
    		if(place != null) {
    			ms0 = System.currentTimeMillis();
    			NonPlace nonplace  = nonPlaceBO.retrieveNonPlaceByDbPediaId(nonplaceId);
    			ms1 = System.currentTimeMillis();
    			LoadDBPediaNonPlacesRelationships.LOGGER.info("@TIME@RETRIVING NON-PLACE:\t" + (ms1-ms0) + "ms");
    			
    			if(nonplace != null) {
    				Place2NPlaceRel edge = placeBO.addRelatedNonPlace(place, nonplace, predicate);
    				if(edge != null) {
    					LoadDBPediaNonPlacesRelationships.LOGGER.info("EDGE "+ edge +" CREATED:\t" + nonplaceId + "\t" + placeId + "\t" + predicate);
    				} else {
    					LoadDBPediaNonPlacesRelationships.LOGGER.error("FAILED TO CREATE EDGE:\t" + nonplaceId + "\t" + placeId + "\t" + predicate);
    				}
    			} else {
    				LoadDBPediaNonPlacesRelationships.LOGGER.error("Missing NONPLACE:\t" + nonplaceId + "\t" + placeId + "\t" + predicate);
    			}
    		} else {
    			LoadDBPediaNonPlacesRelationships.LOGGER.error("Missing PLACE:\t" + nonplaceId + "\t" + placeId + "\t" + predicate);
    		}

    		if(count++ % 10000 == 0) {
    			System.out.print("Commiting.... Actual Count: " + count);
    			graph.getBaseGraph().commit();

    			endTime = System.currentTimeMillis();
    			System.out.print(" - Lap time: " + (endTime - lastTime)/1000.0 + " s");
    			System.out.println(" - Total time: " + (endTime - startTime)/1000.0 + " s");
    			lastTime = endTime;
    		}
    	}
    	
    	graph.getBaseGraph().commit();
    	reader.close();
	}
	
}
