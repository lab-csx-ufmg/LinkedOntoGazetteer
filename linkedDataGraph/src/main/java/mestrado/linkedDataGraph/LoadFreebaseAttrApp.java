package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.business.NameBO;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.rdf.FreebasePredicate;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class LoadFreebaseAttrApp {

	private static final String SUCCESS = "[SUCCESS]\t";
	private static final String FAILED = "[FAILED]\t";
	private static Logger LOGGER = Logger.getLogger(LoadFreebaseAttrApp.class);
	
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
    	NameBO nameBO = new NameBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("freebase.attrs.fpath")));
    	
    	String line;

    	double count = 0;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;
    	
    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split("\t");
    		String subjectId = split[0].substring("http://rdf.freebase.com/ns/".length());
    		String logMsg = "";
    		String result = "";
    		boolean toLog = false;
    		
    		String predicate = split[1];
    		logMsg += predicate + "\t\t\t";
    		
    		if(predicate.equals(FreebasePredicate.NAME) || predicate.equals(FreebasePredicate.ALIAS)) {
    			toLog = true;
				String name = split[2].replaceAll("[\"]", "").replaceAll("@..", "");
				name = StringEscapeUtils.unescapeJava(name);
				
				Place place = placeBO.retrievePlaceByFreebaseId(subjectId);
				
				if(place != null) {
					Name placeName = nameBO.createNameIfNotExists(name);
					placeBO.addPlaceName(place, placeName);
					logMsg += Type.PLACE + "\t";
					result = LoadFreebaseAttrApp.SUCCESS;
				} else {
					logMsg += Type.NONPLACE + "\t";
					result = LoadFreebaseAttrApp.FAILED;
				}
				
				logMsg += name + " ("+subjectId+")";
				
			} else if(predicate.equals(FreebasePredicate.CONTAINS)) {
				toLog = true;
				String objectId = split[2].substring("http://rdf.freebase.com/ns/".length());

				if(objectId.equals(subjectId)) {
					LoadFreebaseAttrApp.LOGGER.info(LoadFreebaseAttrApp.FAILED +  "CONTAINS self relationship ["+ subjectId +"].");
					continue;
				}
				
				Place subjectPlace = placeBO.retrievePlaceByFreebaseId(subjectId);
				Place objPlace = placeBO.retrievePlaceByFreebaseId(objectId);
				
				if(subjectPlace != null && objPlace != null) {
					placeBO.addPlaceContainedBy(objPlace, subjectPlace);
					result = LoadFreebaseAttrApp.SUCCESS;
				} else {
					result = LoadFreebaseAttrApp.FAILED;
				}
				
				logMsg += (subjectPlace != null ? subjectId : "null(" + subjectId + ")") + "--CONTAINED_BY-->" + (objPlace != null ? objectId : "null("+ objectId +")");
//				logMsg += objectId + "--CONTAINED_BY-->"  + subjectId;
				
			} else if(predicate.equals(FreebasePredicate.CONTAINED_BY)) {
				toLog = true;
				String objectId = split[2].substring("http://rdf.freebase.com/ns/".length());

				if(objectId.equals(subjectId)) {
					LoadFreebaseAttrApp.LOGGER.info(LoadFreebaseAttrApp.FAILED +  "CONTAINED_BY self relationship ["+ subjectId +"].");
					continue;
				}
				
				Place subjectPlace = placeBO.retrievePlaceByFreebaseId(subjectId);
				Place objPlace = placeBO.retrievePlaceByFreebaseId(objectId);
				
				if(subjectPlace != null && objPlace != null) {
					placeBO.addPlaceContainedBy(subjectPlace, objPlace);
					result = LoadFreebaseAttrApp.SUCCESS;
				} else {
					result = LoadFreebaseAttrApp.FAILED;
				}
				
				logMsg += (subjectPlace != null ? subjectId : "null(" + subjectId + ")") + "--CONTAINED_BY-->" + (objPlace != null ? objectId : "null("+ objectId +")");
//				logMsg += subjectId + "--CONTAINED_BY-->"  + objectId;
			} else if(predicate.equals(FreebasePredicate.OFFICIAL_WEBSITE)) {
    			toLog = true;
				String officialWS = split[2];

				Place place = placeBO.retrievePlaceByFreebaseId(subjectId);
				
				if(place != null) {
					place.setOfficialWebSite(officialWS);
					result = LoadFreebaseAttrApp.SUCCESS;
				}
				logMsg += officialWS + " ("+subjectId+")";
			}
    		
    		if(toLog) {
    			LoadFreebaseAttrApp.LOGGER.info(result + logMsg);
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
