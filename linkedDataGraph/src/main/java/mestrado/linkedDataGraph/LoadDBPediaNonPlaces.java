package mestrado.linkedDataGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import mestrado.linkedDataGraph.business.NameBO;
import mestrado.linkedDataGraph.business.NonPlaceBO;
import mestrado.linkedDataGraph.business.PlaceBO;
import mestrado.linkedDataGraph.core.rdf.DBPPredicate;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.NonPlace;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class LoadDBPediaNonPlaces {

	private static Logger LOGGER = Logger.getLogger(LoadDBPediaNonPlaces.class);
	
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
    	NonPlaceBO nonPlaceBO = new NonPlaceBO(graph);
    	NameBO nameBO = new NameBO(graph);
    	PlaceBO placeBO = new PlaceBO(graph);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("dbpedia.nonPlace.name.fpath")));
    	
    	String line;
    	double count = 1;
    	double startTime = System.currentTimeMillis(), endTime, lastTime = startTime;

    	while((line = reader.readLine()) != null) {
    		line = line.replaceAll("[<>]", "");
    		String[] split = line.split(" ");
    		String subjectId = split[0].substring("http://dbpedia.org/resource/".length());
    		
    		DBPPredicate predicate = DBPPredicate.valueOf(split[1].substring(split[1].lastIndexOf('/')+1).toUpperCase());
    		
    		if(predicate.equals(DBPPredicate.NAME) || predicate.equals(DBPPredicate.NICK)) {
    			String nameStr = StringEscapeUtils.unescapeJava(split[2]);
				nameStr = nameStr.replaceAll("[\"]", "").replaceAll("@..", "");
				Place place = placeBO.retrievePlaceByDBPediaId(subjectId);
				if(place == null) {
					NonPlace nonplace = nonPlaceBO.createDBPediaNonPlaceIfNotExists(subjectId);
					Name name = nameBO.createNameIfNotExists(nameStr);
					nonPlaceBO.addNonPlaceName(nonplace, name);
					
					LoadDBPediaNonPlaces.LOGGER.info("NonPlace created!\t" + subjectId + "\t" + nonplace + "\t<-name--\t" + name);
				} else {
					LoadDBPediaNonPlaces.LOGGER.info("NonPlace id is from a Place!\t" + subjectId);
				}
			}
    		
			if(count++ % 1000 == 0) {
    			System.out.print("Commiting.... Actual Count: " + count);
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
