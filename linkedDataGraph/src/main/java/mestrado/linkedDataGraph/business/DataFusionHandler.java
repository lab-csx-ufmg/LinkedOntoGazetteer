package mestrado.linkedDataGraph.business;

import org.apache.log4j.Logger;

import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.Place;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;


public class DataFusionHandler {

	private static final Logger LOGGER = Logger.getLogger(DataFusionHandler.class);
	private PlaceBO placeBO;
	
	public DataFusionHandler(FramedGraph<TitanGraph> graph) {
		this.placeBO = new PlaceBO(graph);
	}
	
	public boolean handleDBGeoFusion(String dbpediaId, String geonamesId) {
		boolean result = false;
		String resultStr = "FAILED\t";
		String tagMsg = "DBGEOFUSION";
		
		Place dbpediaPlace = this.placeBO.retrievePlaceByDBPediaId(dbpediaId);
		Place geonamesPlace = this.placeBO.retrievePlaceByGeonamesId(geonamesId);
		if(dbpediaPlace != null && geonamesPlace != null && !dbpediaPlace.equals(geonamesPlace)) {
			//Copy all contained By places
			new CopyRelationship<Place, Place>() {
				public void copy(Place to, Place object) {
					to.addContainedBy(object);
				}
			}.copyFrom(dbpediaPlace, geonamesPlace.getContainedBy());
			
			//Copy all contains places
			new CopyRelationship<Place, Place>() {
				public void copy(Place to, Place object) {
					to.addContains(object);
				}
			}.copyFrom(dbpediaPlace, geonamesPlace.getContains());
			
			//Copy all countries 
			new CopyRelationship<Place, Place>() {
				public void copy(Place to, Place object) {
					to.addCountry(object);
				}
			}.copyFrom(dbpediaPlace, geonamesPlace.getCountries());
			
			//Copy all names
			new CopyRelationship<Name, Place>() {
				public void copy(Place to, Name object) {
					to.addName(object);
				}
			}.copyFrom(dbpediaPlace, geonamesPlace.getNames());
			
			if(geonamesPlace.getFreebaseId() != null) {
				//TODO copy property
			}
			
			if(geonamesPlace.getOntogztId() != null) {
				//TODO copy property
			}
			
			this.placeBO.removePlace(geonamesPlace.asVertex());
			//TODO copy other properties
			dbpediaPlace.setGeonamesId(geonamesId);
			
			result = true;
			resultStr = "SUCCESS\t";
			
		} else if(geonamesPlace != null && dbpediaPlace == null) {
			geonamesPlace.setDbpediaId(dbpediaId);
			
			tagMsg += "(PropAdd)";
			
			
		} else if(geonamesPlace == null && dbpediaPlace != null) {
			dbpediaPlace.setGeonamesId(geonamesId);
			
			tagMsg += "(PropAdd)";
		}
		
		DataFusionHandler.LOGGER.info(resultStr + tagMsg + "\t" + dbpediaPlace + "<-->" + geonamesPlace + "\t" + dbpediaId + "<-->" + geonamesId);
		
		return result;
	}
}
