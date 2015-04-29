package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.business.NameBO;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.Name;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class FixEncodedNames {

	private static Logger LOGGER = Logger.getLogger(FixEncodedNames.class);
	
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
    	NameBO nameBO = new NameBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("names.fix.fpath")));
    	
    	String line;

    	double count = 0;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;
    	
    	while((line = reader.readLine()) != null) {
    		Name vertexByName = nameBO.getVertexByName(line.trim());
    		
    		if(vertexByName != null) {
    			String unescapeJava = StringEscapeUtils.unescapeJava(line);
    			Name unescape = nameBO.getVertexByName(unescapeJava.trim());
    			
    			if(unescape == null) {
    				FixEncodedNames.LOGGER.info("FIX\t" + line.trim() + "\t" + unescapeJava.trim());
    				vertexByName.setName(unescapeJava);
    			} else {
    				FixEncodedNames.LOGGER.info("TO MERGE\t" + line.trim() + "\t" + unescapeJava.trim());
    			}
    		} else {
    			FixEncodedNames.LOGGER.info("UNABLE TO FIND\t" + line.trim());
    		}
    		
    		if(count++ % 10000 == 0) {
    			System.out.print(count);
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