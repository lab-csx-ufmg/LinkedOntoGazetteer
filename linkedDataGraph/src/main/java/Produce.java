import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;

import mestrado.linkedDataGraph.jms.GeonamesPoint;
import mestrado.linkedDataGraph.jms.JMSProducer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Produce {
	
	private static final Logger LOGGER = Logger.getLogger(Produce.class);
	
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4jfilter.properties");
		
		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
		
		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("geonames.coord.file")));
		
		String line;
		JMSProducer<GeonamesPoint> jmsProducer = new JMSProducer<GeonamesPoint>("myQ");
		
		while((line = reader.readLine()) != null) {
			String[] split = line.split("\t");
			
			GeonamesPoint geonamesP = new GeonamesPoint(split[0], split[1], split[2]);
			jmsProducer.send(geonamesP);
			
			Produce.LOGGER.info(geonamesP.toString());
		}
		
		reader.close();
		jmsProducer.close();
	}
	
}
