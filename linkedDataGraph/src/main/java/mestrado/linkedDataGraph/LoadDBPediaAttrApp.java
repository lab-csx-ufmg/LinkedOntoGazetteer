package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.business.NameBO;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.rdf.DBPPredicate;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class LoadDBPediaAttrApp {

	private static final String SUCCESS = "[SUCCESS]\t";
	private static final String FAILED = "[FAILED]\t";
	private static Logger LOGGER = Logger.getLogger(LoadDBPediaAttrApp.class);
	
	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4jfilter.properties");
		
		Properties p = new Properties(System.getProperties());
		p.load(new FileInputStream(new File("app.properties")));
		System.setProperties(p);
		
    	BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandra");
		config.setProperty("storage.hostname","127.0.0.1");
    	
    	FramedGraph<TitanGraph> graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
    	PlaceBO placeBO = new PlaceBO(graph);
    	NameBO nameBO = new NameBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("dbpedia.attrs.fpath")));
    	
    	String line;

    	double count = 0;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;
    	
    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split("\t");
    		String subjectId = split[0].substring("http://dbpedia.org/resource/".length());
    		String logMsg = "";
    		String result = "";
    		boolean toLog = false;
    		
    		DBPPredicate predicate = DBPPredicate.valueOf(split[1].substring(split[1].lastIndexOf('/')+1).toUpperCase());
    		logMsg += predicate.getUri() + "\t\t\t";
    		
    		if(predicate.equals(DBPPredicate.NAME) || predicate.equals(DBPPredicate.NICK)) {
    			toLog = true;
				String name = split[2].replaceAll("[\"]", "").replaceAll("@en", "");
				
				
				Place place = placeBO.retrievePlaceByDBPediaId(subjectId);
				
				if(place != null) {
					Name placeName = nameBO.createNameIfNotExists(name);
					placeBO.addPlaceName(place, placeName);
					logMsg += Type.PLACE + "\t";
					result = SUCCESS;
				} else {
					logMsg += Type.NONPLACE + "\t";
					result = FAILED;
				}
				
				logMsg += name + " ("+subjectId+")";
				
			} else if(predicate.equals(DBPPredicate.COUNTRY)) {
				toLog = true;
				String objectId = split[2].substring("http://dbpedia.org/resource/".length());
				if(objectId.equals(subjectId)) {
					LoadDBPediaAttrApp.LOGGER.info(FAILED + "COUNTRY self relationship ["+ subjectId +"].");
					continue;
				}
				
				Place subjectPlace = placeBO.retrievePlaceByDBPediaId(subjectId);
				Place objPlace = placeBO.retrievePlaceByDBPediaId(objectId);
				
				if(subjectPlace != null && objPlace != null) {
					placeBO.addPlaceCountry(subjectPlace, objPlace);
					result = SUCCESS;
				} else {
					result = FAILED;
				}

				logMsg += (subjectPlace != null ? subjectId : "null(" + subjectId + ")") + "--COUNTRY-->" + (objPlace != null ? objectId : "null(" + objectId + ")");
				
			} else if(predicate.equals(DBPPredicate.ISPARTOF) || predicate.equals(DBPPredicate.LOCATION) ||
					predicate.equals(DBPPredicate.REGION) || predicate.equals(DBPPredicate.STATE)) {
				toLog = true;
				String objectId = split[2].substring("http://dbpedia.org/resource/".length());

				if(objectId.equals(subjectId)) {
					LoadDBPediaAttrApp.LOGGER.info(FAILED +  "CONTAINED_BY self relationship ["+ subjectId +"].");
					continue;
				}
				
				Place subjectPlace = placeBO.retrievePlaceByDBPediaId(subjectId);
				Place objPlace = placeBO.retrievePlaceByDBPediaId(objectId);
				
				if(subjectPlace != null && objPlace != null) {
					placeBO.addPlaceContainedBy(subjectPlace, objPlace);
					result = SUCCESS;
				} else {
					result = FAILED;
				}
				
				logMsg += (subjectPlace != null ? subjectId : "null(" + subjectId + ")") + "--CONTAINED_BY-->" + (objPlace != null ? objectId : "null("+ objectId +")");
			}				
    		
    		if(toLog) {
    			LoadDBPediaAttrApp.LOGGER.info(result + logMsg);
    		}
    		
    		if(count++ % 10000 == 0) {
    			System.out.print((count/4575942.0)*100.0 + "%");
    			graph.getBaseGraph().commit();

    			endTime = System.currentTimeMillis();
    			System.out.print(" - Lap time: " + (endTime - lastTime)/1000.0 + " s");
    			System.out.println(" - Total time: " + (endTime - startTime)/1000.0 + " s");
    			lastTime = endTime;
    		}
    		
    	}
    	
    	reader.close();
    	
    	graph.shutdown();
	}
}
