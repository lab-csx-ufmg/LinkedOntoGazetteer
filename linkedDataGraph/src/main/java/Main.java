import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;

import mestrado.linkedDataGraph.LinkedOntoGztSchema;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.jms.GeonamesDetails;
import mestrado.linkedDataGraph.jms.JMSConsumer;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	public static void main(String[] args) throws FileNotFoundException, IOException, JMSException, InterruptedException {
		PropertyConfigurator.configure("log4jfilter.properties");
		
		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
		
		BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandrathrift");
		config.setProperty("storage.hostname", System.getProperty("storage.hostname"));
		config.setProperty("storage.index.search.backend","elasticsearch");
		config.setProperty("storage.index.search.directory","db/es");
		config.setProperty("storage.index.search.client-only",false);
		config.setProperty("storage.index.search.local-mode",true);

		
		JMSConsumer<GeonamesDetails> jmsConsumer = new JMSConsumer<GeonamesDetails>(GeonamesDetails.class, "geonamesDetails");
		
		FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);

		PlaceBO placeBO = new PlaceBO(graph);

		while(true) {
			GeonamesDetails msg = jmsConsumer.receive();
			
			Place place = placeBO.retrievePlaceByGeonamesId(msg.getGeonamesId());
			
			if(place == null) {
				Main.LOGGER.error("Nao existe: \t" + msg.getGeonamesId());
				continue;
			}
			
			place.setGeonamesFeatureClass(msg.getFeatureClass());
			place.setGeonamesFeatureCode(msg.getFeatureCode());
			graph.getBaseGraph().commit();
			
			Main.LOGGER.info(msg.getGeonamesId() + "\t" + place.toString());
		}
		
//		graph.shutdown();		
	}

}
