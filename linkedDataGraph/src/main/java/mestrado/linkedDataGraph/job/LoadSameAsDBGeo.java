package mestrado.linkedDataGraph.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mestrado.linkedDataGraph.core.BatchLoadJob;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.log4j.Logger;

public class LoadSameAsDBGeo extends BatchLoadJob {
	private static final Pattern idPattern = Pattern.compile("[0-9]+");
	private static final Logger LOGGER = Logger.getLogger(LoadSameAsDBGeo.class);
	
	public LoadSameAsDBGeo() {
		super("sameas.dbgeo.fpath");
	} 

	public String getId(String txt) {
		Matcher m = LoadSameAsDBGeo.idPattern.matcher(txt);
    	m.find();
    	
    	return m.group();
	}
	
	@Override 
	public boolean processLine(String line) {
		line = line.replaceAll("[<>]", "");
		String[] split = line.split(" ");
		if(split.length != 4) {
			LoadSameAsDBGeo.LOGGER.error("Missing elemet, size: " + split.length + ". line: " + line);
			return false;
		}
		
		String dbpediaId = split[0].substring("http://dbpedia.org/resource/".length());
		String geonamesId = this.getId(split[2]);
	
		Place dbpediaPlace = this.placeBO.retrievePlaceByDBPediaId(dbpediaId);
		Place geonamesPlace = this.placeBO.retrievePlaceByGeonamesId(geonamesId);
		if(dbpediaPlace == null && geonamesPlace == null) {
			geonamesPlace = this.placeBO.createGeoNamesPlaceIfNotExists(geonamesId);
			geonamesPlace.setDbpediaId(dbpediaId);
			LoadSameAsDBGeo.LOGGER.info("[GEODB] SAME_AS LINK\t" + geonamesId + "\t" + dbpediaId);
		} else if(geonamesPlace == null){
			LoadSameAsDBGeo.LOGGER.info("[DBGEO] SAME_AS LINK\t" + geonamesId + "\t" + dbpediaId);
			dbpediaPlace.setGeonamesId(geonamesId);
		} else if (geonamesPlace != null) {
			LoadSameAsDBGeo.LOGGER.error("[DBGEO] SAME_AS LINK\t" + geonamesId + "\t" + dbpediaId + "\tTwo places to same geonamesId");
		}
		
		return true;
	}

	
}
