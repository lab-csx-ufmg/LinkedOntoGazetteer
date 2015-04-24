import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.LinkedOntoGztSchema;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;


public class InitGraph {

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
		
    	graph.shutdown();
	}
	
}
