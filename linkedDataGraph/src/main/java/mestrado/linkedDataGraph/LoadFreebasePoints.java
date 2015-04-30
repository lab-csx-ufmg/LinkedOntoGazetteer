package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.rdf.FreebasePredicate;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.frames.FramedGraph;

public class LoadFreebasePoints {

	private static Logger LOGGER = Logger.getLogger(LoadFreebasePoints.class);
	
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
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("freebase.points.fpath")));
    	
    	String line;

    	String curLat = null, curLng = null;
    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split("\t");
    		String subjectId = split[0].substring("http://rdf.freebase.com/ns/".length());
    		
    		String predicate = split[1];
    		
    		if(predicate.equals(FreebasePredicate.LONGITUDE)) {
    			line = reader.readLine();
    			curLng = split[2].replaceAll("\"", "");
    			curLat = line.split("\t")[2].replaceAll("\"", "");
			} else if(predicate.equals(FreebasePredicate.LATITUDE)) {
				line = reader.readLine();
				curLat = split[2].replaceAll("\"", "");
    			curLng = line.split("\t")[2].replaceAll("\"", "");
			}
    		
    		Place retrievePlaceByFreebaseId = placeBO.retrievePlaceByFreebaseId(subjectId);
    		Geoshape point = Geoshape.point(Double.valueOf(curLat), Double.valueOf(curLng));

    		if(retrievePlaceByFreebaseId != null) {
				LoadFreebasePoints.LOGGER.info(subjectId + "\t" + point);
    			retrievePlaceByFreebaseId.setFreebasePoint(point);
    		} else {
    			LoadFreebasePoints.LOGGER.error(subjectId + "\t" + point);
    		}
    	}
    	
    	graph.getBaseGraph().commit();
    	reader.close();
	}
}
