package mestrado.linkedDataGraph.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.LinkedOntoGztSchema;
import mestrado.linkedDataGraph.business.NameBO;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public abstract class Job {

	protected PlaceBO placeBO;
	protected NameBO nameBO;
	protected FramedGraph<TitanGraph> graph;
	
	public Job() {
		try {
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
			
			this.graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
			this.placeBO = new PlaceBO(this.graph);
			this.nameBO = new NameBO(this.graph);
		} catch (IOException e) {
			throw new RuntimeException("Unable to instantiate the Job. " + this.getClass(), e);
		}
	}
	
	
	public abstract void execute();
	
}
