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

public class FixDBFreebaseId {
	private static final Logger LOGGER = Logger.getLogger(FixDBFreebaseId.class);
	private static final String EMPTY_VALUE = "###";
	
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
    	PlaceBO placeBO = new PlaceBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("tofix.fpath")));
    	
    	String line;

    	double count = 1;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;
    	
    	while((line = reader.readLine()) != null) {
    		String[] split = line.split("\t");
    		
    		if(split.length != 3) {
    			FixDBFreebaseId.LOGGER.error("@FIXFBIDSTATISTIC@\tMissing elemet, size: " + split.length + ". line: " + line);
    			continue;
    		}
    		
    		String freebaseId = split[0];
    		String dbpediaId = split[1].replace("%25", "%");
    		String geonamesId = split[2];
    		Place freebasePlace = null;
    		Place dbpediaPlace = null;
    		Place geonamesPlace = null;

    		if(geonamesId.equals(FixDBFreebaseId.EMPTY_VALUE) && dbpediaId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
    			freebasePlace = placeBO.retrievePlaceByGeonamesId(freebaseId);
    			if(freebasePlace != null) {
    				FixDBFreebaseId.LOGGER.info("@FIXFBIDSTATISTIC@\tFIXED\t" + freebaseId);
    				freebasePlace.setFreebaseId(freebaseId);
    				freebasePlace.setGeonamesId(null);
    			} else {
    				FixDBFreebaseId.LOGGER.info("@FIXFBIDSTATISTIC@\tALREADY_FIXED\t" + freebaseId);	
    			}
    		} else {
	    		
    			freebasePlace = placeBO.retrievePlaceByGeonamesId(freebaseId);
	    		
	    		if(freebasePlace != null) {
	    			placeBO.removePlace(freebasePlace.asVertex());
	    			
	    			if(geonamesId.equals(FixDBFreebaseId.EMPTY_VALUE) && dbpediaId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
	        			FixDBFreebaseId.LOGGER.info("@STATISTIC@\tNO SAME AS\tCREATING FREEBASE VERTEX\t" + freebaseId );
	        			freebasePlace = placeBO.createFreebasePlaceIfNotExists(freebaseId);
	        			continue;
	        		}
	        		
	        		if(!dbpediaId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
	        			dbpediaPlace = placeBO.retrievePlaceByDBPediaId(dbpediaId);
	        		}

	        		if(!geonamesId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
	        			geonamesPlace = placeBO.retrievePlaceByGeonamesId(geonamesId);
	        		}
	        		
	        		if(dbpediaPlace != null && geonamesPlace != null) {
	        			if(dbpediaPlace.asVertex().getId().equals(geonamesPlace.asVertex().getId())) {
	        				dbpediaPlace.setFreebaseId(freebaseId);
	        				FixDBFreebaseId.LOGGER.info("@STATISTIC@\t[BOTH]\t" + freebaseId + "\t" + dbpediaId + "\t" + geonamesId);
	        			} else {
	        				//tem de fazer merge
	        				FixDBFreebaseId.LOGGER.info("@STATISTIC@\t[TO_MERGE]\t" + freebaseId + "\t" + dbpediaId + "\t" + geonamesId);
	        			}
	        		} else if(dbpediaPlace != null && geonamesPlace == null) {
	        			if(!geonamesId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
	        				dbpediaPlace.setGeonamesId(geonamesId);
	        			}
	        			dbpediaPlace.setFreebaseId(freebaseId);
	        			
	        			FixDBFreebaseId.LOGGER.info("@STATISTIC@\t[DBPEDIA]\t" + freebaseId + "\t" + dbpediaId + "\t" + geonamesId);
	    			} else if(dbpediaPlace == null && geonamesPlace != null) {
	    				if(!dbpediaId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
	    					geonamesPlace.setDbpediaId(dbpediaId);
	    				}
	    				geonamesPlace.setFreebaseId(freebaseId);
	    				
	    				FixDBFreebaseId.LOGGER.info("@STATISTIC@\t[GEONAMES]\t" + freebaseId + "\t" + dbpediaId + "\t" + geonamesId);
	    			} else if(dbpediaPlace == null && geonamesPlace == null) {
	    				freebasePlace = placeBO.createFreebasePlaceIfNotExists(freebaseId);

	    				if(!dbpediaId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
	    					freebasePlace.setDbpediaId(dbpediaId);
	    				}
	    				
	        			if(!geonamesId.equals(FixDBFreebaseId.EMPTY_VALUE)) {
	        				freebasePlace.setGeonamesId(geonamesId);
	        			}
	    				
	        			FixDBFreebaseId.LOGGER.info("@STATISTIC@\t[NEW VERTEX CREATED]\t" + freebaseId + "\t" + dbpediaId + "\t" + geonamesId);
	    			}
	    			
				} else {
					FixDBFreebaseId.LOGGER.info("@FIXFBIDSTATISTIC@\tNULL\t" + freebaseId);
				}
    		}
    		
    		if(count++ % 5000 == 0) {
    			graph.getBaseGraph().commit();

    			endTime = System.currentTimeMillis();
    			System.out.print(" - Lap time: " + (endTime - lastTime)/1000.0 + " s");
    			System.out.println(" - Total time: " + (endTime - startTime)/1000.0 + " s");
    			lastTime = endTime;
    		}
    	}
    	
    	FixDBFreebaseId.LOGGER.info("LAST COMMIT");
    	graph.getBaseGraph().commit();
    	
    	reader.close();
	}
}
