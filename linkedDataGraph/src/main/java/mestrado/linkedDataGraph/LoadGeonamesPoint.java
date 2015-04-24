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
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.frames.FramedGraph;

public class LoadGeonamesPoint {

	private static Logger LOGGER = Logger.getLogger(LoadGeonamesPoint.class);
	
	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4jfilter.properties");
		
		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
		
    	BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandra");
		config.setProperty("storage.hostname","127.0.0.1");
		config.setProperty("storage.index.search.backend","elasticsearch");
		config.setProperty("storage.index.search.directory","/tmp/searchindex");
		config.setProperty("storage.index.search.client-only",false);
		config.setProperty("storage.index.search.local-mode",true);
		
    	FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
    	PlaceBO placeBO = new PlaceBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("geopoints.fpath")));
    	
    	String line;
    	
    	double count = 0;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;
    	
    	while((line = reader.readLine()) != null) {
    		String[] split = line.split("\t");
    		
    		Place place = placeBO.retrievePlaceByGeonamesId(split[0]);
    		
    		if(place != null && place.getGeonamesPoint() == null) {
    			LOGGER.info("Point:\t" + split[0]);
    			place.setGeonamesPoint(Geoshape.point(Double.valueOf(split[1]), Double.valueOf(split[2])));
    		}
    		
    		if(count++ % 1000 == 0) {
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
 