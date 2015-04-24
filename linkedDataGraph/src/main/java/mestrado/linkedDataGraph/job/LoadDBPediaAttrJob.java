package mestrado.linkedDataGraph.job;

import mestrado.linkedDataGraph.core.BatchLoadJob;
import mestrado.linkedDataGraph.core.rdf.Predicate;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.log4j.Logger;

public class LoadDBPediaAttrJob extends BatchLoadJob {
 
	private static final String SUCCESS = "[SUCCESS]\t";
	private static final String FAILED = "[FAILED]\t";
	private static Logger LOGGER = Logger.getLogger(LoadDBPediaAttrJob.class);
	
	public LoadDBPediaAttrJob() {
		super("dbpedia.attrs.fpath");
	}
	
	@Override
	public boolean processLine(String line) {
		line = line.replaceAll("[<>]", "");
		String[] split = line.split("\t");
		String subjectId = split[0].substring("http://dbpedia.org/resource/".length());
		String logMsg = "";
		String result = "";
		boolean toLog = false;
		
		Predicate predicate = Predicate.valueOf(split[1].substring(split[1].lastIndexOf('/')+1).toUpperCase());
		logMsg += predicate.getUri() + "\t\t\t";
		
		if(predicate.equals(Predicate.NAME) || predicate.equals(Predicate.NICK)) {
			toLog = true;
			String name = split[2].replaceAll("[\"]", "").replaceAll("@en", "");
			
			
			Place place = this.placeBO.retrievePlaceByDBPediaId(subjectId);
			
			if(place != null) {
				Name placeName = this.nameBO.createNameIfNotExists(name);
				this.placeBO.addPlaceName(place, placeName);
				logMsg += Type.PLACE + "\t";
				result = LoadDBPediaAttrJob.SUCCESS;
			} else {
				logMsg += Type.NONPLACE + "\t";
				result = LoadDBPediaAttrJob.FAILED;
			}
			
			logMsg += name + " ("+subjectId+")";
			
		} else if(predicate.equals(Predicate.COUNTRY)) {
			toLog = true;
			String objectId = split[2].substring("http://dbpedia.org/resource/".length());
			if(objectId.equals(subjectId)) {
				LoadDBPediaAttrJob.LOGGER.info(LoadDBPediaAttrJob.FAILED + "COUNTRY self relationship ["+ subjectId +"].");
				return false;
			}
			
			Place subjectPlace = this.placeBO.retrievePlaceByDBPediaId(subjectId);
			Place objPlace = this.placeBO.retrievePlaceByDBPediaId(objectId);
			
			if(subjectPlace != null && objPlace != null) {
				this.placeBO.addPlaceCountry(subjectPlace, objPlace);
				result = LoadDBPediaAttrJob.SUCCESS;
			} else {
				result = LoadDBPediaAttrJob.FAILED;
			}

			logMsg += (subjectPlace != null ? subjectId : "null(" + subjectId + ")") + "--COUNTRY-->" + (objPlace != null ? objectId : "null(" + objectId + ")");
			
		} else if(predicate.equals(Predicate.ISPARTOF) || predicate.equals(Predicate.LOCATION) ||
				predicate.equals(Predicate.REGION) || predicate.equals(Predicate.STATE)) {
			toLog = true;
			String objectId = split[2].substring("http://dbpedia.org/resource/".length());

			if(objectId.equals(subjectId)) {
				LoadDBPediaAttrJob.LOGGER.info(LoadDBPediaAttrJob.FAILED +  "CONTAINED_BY self relationship ["+ subjectId +"].");
				return false;
			}
			
			Place subjectPlace = this.placeBO.retrievePlaceByDBPediaId(subjectId);
			Place objPlace = this.placeBO.retrievePlaceByDBPediaId(objectId);
			
			if(subjectPlace != null && objPlace != null) {
				this.placeBO.addPlaceContainedBy(subjectPlace, objPlace);
				result = LoadDBPediaAttrJob.SUCCESS;
			} else {
				result = LoadDBPediaAttrJob.FAILED;
			}
			
			logMsg += (subjectPlace != null ? subjectId : "null(" + subjectId + ")") + "--CONTAINED_BY-->" + (objPlace != null ? objectId : "null("+ objectId +")");
		}				
		
		if(toLog) {
			LoadDBPediaAttrJob.LOGGER.info(result + logMsg);
		}
		
		return true;
	}
}
