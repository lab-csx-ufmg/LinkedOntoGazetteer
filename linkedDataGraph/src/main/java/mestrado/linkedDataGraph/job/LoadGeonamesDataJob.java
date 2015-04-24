package mestrado.linkedDataGraph.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mestrado.linkedDataGraph.business.DataFusionHandler;
import mestrado.linkedDataGraph.core.BatchLoadJob;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.log4j.Logger;

public class LoadGeonamesDataJob extends BatchLoadJob {

	private static final Pattern idPattern = Pattern.compile("[0-9]+"); 
	 
	private static final Logger LOGGER = Logger.getLogger(LoadGeonamesDataJob.class);
	private final DataFusionHandler dataFusionHandler;

	public LoadGeonamesDataJob() {
		super("geonames.fpath");
		this.dataFusionHandler = new DataFusionHandler(this.graph);
	}
	
	public String getId(String txt) {
		Matcher m = LoadGeonamesDataJob.idPattern.matcher(txt);
    	m.find();
    	
    	return m.group();
	}

	@Override
	public boolean processLine(String line) {
		
		line = line.replaceAll("[<>]", "");
		String[] split = line.split("\t");
		String subjectId = this.getId(split[0]);
		
		if(line.contains("gn:name")) {
			LoadGeonamesDataJob.LOGGER.debug(line);
			Name name = this.nameBO.createNameIfNotExists(split[2].trim());
			Place place = this.placeBO.createGeoNamesPlaceIfNotExists(subjectId);
			this.placeBO.addPlaceName(place, name);
			
		} else if(line.contains("gn:alternateName")) {
			LoadGeonamesDataJob.LOGGER.debug(line);
        	for(String namestr : split[2].split(",")) {
        		
        		
    			Name name = this.nameBO.createNameIfNotExists(namestr.trim());
    			Place place = this.placeBO.createGeoNamesPlaceIfNotExists(subjectId);
    			this.placeBO.addPlaceName(place, name);
        	}
        	
        } else if(line.contains("gn:parentFeature") || line.contains("gn:parentADM")) {
        	LoadGeonamesDataJob.LOGGER.debug(line);

        	String objectId = this.getId(split[2]);

        	Place place = this.placeBO.createGeoNamesPlaceIfNotExists(subjectId);
        	Place containedBy = this.placeBO.createGeoNamesPlaceIfNotExists(objectId);
        	
        	this.placeBO.addPlaceContainedBy(place, containedBy);
        	
        	
        } else if(line.contains("gn:wikipediaArticle")) {
        	LoadGeonamesDataJob.LOGGER.debug(line);
        	
        	for(String url : split[2].split(",")) {
        		if(url.startsWith("http://en.wikipedia.org/wiki/")) {
        			this.dataFusionHandler.handleDBGeoFusion(url.substring("http://en.wikipedia.org/wiki/".length()), subjectId);
        			break;
        		}
        	}
        	
        } else if(line.contains("gn:parentCountry")) {
        	String objectId = this.getId(split[2]);
        	
        	Place place = this.placeBO.createGeoNamesPlaceIfNotExists(subjectId);
        	Place country = this.placeBO.createGeoNamesPlaceIfNotExists(objectId);
        	
        	this.placeBO.addPlaceCountry(place, country);
        }
		
		return true;
	}

	
}
