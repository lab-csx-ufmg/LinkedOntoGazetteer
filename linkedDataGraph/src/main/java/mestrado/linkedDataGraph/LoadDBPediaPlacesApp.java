package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.EntityType;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;
public class LoadDBPediaPlacesApp {
    public static void main(String[] args) throws IOException {
    	PropertyConfigurator.configure("log4jfilter.properties");
		
    	Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
    	
    	BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandra");
		config.setProperty("storage.hostname","127.0.0.1");
		config.setProperty("storage.batch-loading","true");
		config.setProperty("autotype","none");
		
    	
    	FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("places.fpath")));
    	
    	String line;
    	
    	EntityType placeType = graph.addVertex(null, EntityType.class);
    	placeType.setType(Type.PLACE);

    	double percent = 0.0;
    	
    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split(" ");
    		
    		String dbPediaId = split[0].substring("http://dbpedia.org/resource/".length());
			
    		
			Place place = graph.addVertex(null, Place.class);
			place.setDbpediaId(dbPediaId);
			
			placeType.addEntity(place);
			
			if(percent % 10000 == 0) {
				System.out.println((percent/639450.0)*100 + "%");
				graph.getBaseGraph().commit();
			}
			
			percent++;
    	}
    	
    	reader.close();

    	graph.shutdown();
    	
    }
}
