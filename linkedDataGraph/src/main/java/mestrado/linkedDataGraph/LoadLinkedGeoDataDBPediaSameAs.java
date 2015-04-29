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

public class LoadLinkedGeoDataDBPediaSameAs {

	private static final Logger LOGGER = Logger.getLogger(LoadLinkedGeoDataDBPediaSameAs.class);

	
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
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("sameas.lgd.dbp.fpath")));
    	
    	String line;

    	while((line = reader.readLine()) != null) {
    		String[] split = line.split("\t");
    		
    		String lgdId = split[0].trim();
    		//Strings are prefixed by en:
    		String dbpediaID = split[1].substring(3).trim();
    		
    		Place place = placeBO.retrievePlaceByDBPediaId(dbpediaID);
    		
    		if(place != null) {
    			if(place.getLinkedGeoDataId() != null) {
    				LoadLinkedGeoDataDBPediaSameAs.LOGGER.warn("RESETING...\t" + dbpediaID + "\t" + lgdId);
    			}
    			LoadLinkedGeoDataDBPediaSameAs.LOGGER.info("SUCCESS\t" + dbpediaID + "\t" + lgdId);
    			place.setLinkedGeoDataId(lgdId);
    		} else {
    			LoadLinkedGeoDataDBPediaSameAs.LOGGER.info("FAIL\t" + dbpediaID + "\t" + lgdId);
    		}
    		
    	}
    	
    	graph.getBaseGraph().commit();
    	reader.close();
	}
	
}
