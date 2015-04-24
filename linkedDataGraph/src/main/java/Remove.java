import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.util.TitanCleanup;


public class Remove {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		PropertyConfigurator.configure("log4jfilter.properties");
		
    	Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
    	
    	BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandra");
		config.setProperty("storage.hostname","127.0.0.1");
		config.setProperty("storage.batch-loading","true");
		config.setProperty("autotype","none");
		
    	
		TitanGraph graph = TitanFactory.open(config);
    	graph.shutdown();
    	TitanCleanup.clear(graph);
    	
		
	}
	
}
