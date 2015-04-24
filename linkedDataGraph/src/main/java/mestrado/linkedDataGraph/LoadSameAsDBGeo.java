package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class LoadSameAsDBGeo {

	private static final Logger LOGGER = Logger.getLogger(LoadSameAsDBGeo.class);
	
	public static void main(String[] args) throws IOException {
	
		PropertyConfigurator.configure("log4jfilter.properties");
		
		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
		
		BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandrathrift");
		config.setProperty("storage.hostname","100.74.108.104");
		config.setProperty("storage.index.search.backend","elasticsearch");
		config.setProperty("storage.index.search.directory","../db/es");
		config.setProperty("storage.index.search.client-only",false);
		config.setProperty("storage.index.search.local-mode",true);

    	
    	FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
    	PlaceBO placeBO = new PlaceBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("sameas.dbgeo.fpath")));
    	
    	String line;

    	double count = 0;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;
    	
    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split(" ");
    		if(split.length != 4) {
    			LoadSameAsDBGeo.LOGGER.error("Missing elemet, size: " + split.length + ". line: " + line);
    			continue;
    		}
    		
    		String dbpediaId = split[0].substring("http://dbpedia.org/resource/".length());
    		String geonamesId = LoadGeonamesDataApp.getId(split[2]);
    	
    		
    		Place dbpediaPlace = placeBO.retrievePlaceByDBPediaId(dbpediaId);
    		Place geonamesPlace = placeBO.retrievePlaceByGeonamesId(geonamesId);
    		if(dbpediaPlace == null && geonamesPlace == null) {
    			geonamesPlace = placeBO.createGeoNamesPlaceIfNotExists(geonamesId);
    			geonamesPlace.setDbpediaId(dbpediaId);
    			LoadSameAsDBGeo.LOGGER.info("[GEODB] SAME_AS LINK\t" + geonamesId + "\t" + dbpediaId);
    		} else if(dbpediaPlace != null && geonamesPlace == null){
    			LoadSameAsDBGeo.LOGGER.error("[GEODB] SAME_AS LINK\t" + geonamesId + "\t" + dbpediaId);
    			dbpediaPlace.setGeonamesId(geonamesId);
    		} else if (geonamesPlace != null && dbpediaPlace == null) {
    			LoadSameAsDBGeo.LOGGER.error("[GEODB] SAME_AS LINK\t" + geonamesId + "\t" + dbpediaId);
    			geonamesPlace.setDbpediaId(dbpediaId);
    		} else {
    			LoadSameAsDBGeo.LOGGER.error("[GEODB] SAME_AS LINK\t" + geonamesId + "\t" + dbpediaId + "\tTwo places to same geonamesId");
    		}
    		
    		
    		if(count++ % 5000 == 0) {
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
