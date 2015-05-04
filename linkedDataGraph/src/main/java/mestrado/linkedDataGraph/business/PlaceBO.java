package mestrado.linkedDataGraph.business;

import java.util.Set;

import mestrado.linkedDataGraph.edge.Place2NPlaceRel;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.NonPlace;
import mestrado.linkedDataGraph.vertex.Place;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;

public class PlaceBO extends BusinessObject {

	public PlaceBO(FramedGraph<TitanGraph> graph) {
		super(graph);
	}

	public void addPlaceContainedBy(Place place, Place containedBy) {
		Set<Place> set = this.iterable2Set(place.getContainedBy());
		if(!set.contains(containedBy)) {
			place.addContainedBy(containedBy);
		} else {
			this.getLogger().warn("CONTAINED_BY edge already exists: " + place + "-->" + containedBy);
		}
	}
	
	public void addPlaceCountry(Place place, Place country) {
		Set<Place> set = this.iterable2Set(place.getCountries());
		if(!set.contains(country)) {
			place.addCountry(country);
		} else {
			this.getLogger().warn("COUNTRY edge already exists: " + place + "-->" + country);
		}

		this.addPlaceContainedBy(place, country);
	}
	
	public void addPlaceName(Place place, Name name) {
		Set<Name> set = this.iterable2Set(place.getNames());
		if(!set.contains(name)) {
			place.addName(name);
		} else {
			this.getLogger().warn("Place name already defined: " + name.getName() + "-->" + place);
		}
		
	}
	
	public boolean containsGeoNamesPlace(String geonamesId) {
		return this.graph.getVertices("geonamesId", geonamesId).iterator().hasNext();
	}
	
	public Place createFreebasePlaceIfNotExists(String freebaseId) {
		Place placeVertex = this.getFramedVertex("freebaseId", freebaseId, Place.class);
		
		this.getLogger().debug("@createFreebasePlaceIfNotExists: " + placeVertex);
		
		if(placeVertex == null) {
			placeVertex = this.graph.addVertex(null, Place.class);
			placeVertex.setFreebaseId(freebaseId);
			
//			EntityType placeType = Iterables.getOnlyElement(this.graph.getVertices("entityType", Type.PLACE, EntityType.class));
			
//			placeType.addEntity(placeVertex);
		}
		
		return placeVertex;
	}

	public Place createGeoNamesPlaceIfNotExists(String geonamesId) {
		Place placeVertex = this.getFramedVertex("geonamesId", geonamesId, Place.class);
		
		this.getLogger().debug("@createGeoNamesPlaceIfNotExists: " + placeVertex);
		
		if(placeVertex == null) {
			placeVertex = this.graph.addVertex(null, Place.class);
			placeVertex.setGeonamesId(geonamesId);
			
//			EntityType placeType = Iterables.getOnlyElement(this.graph.getVertices("entityType", Type.PLACE, EntityType.class));
			
//			placeType.addEntity(placeVertex);
		}
		
		return placeVertex;
	}

	public boolean isGeonamesPlaceInfoAdded(String geonamesId) {
		Place place = this.retrievePlaceByGeonamesId(geonamesId);
		return place != null ? place.asVertex().getEdges(Direction.OUT).iterator().hasNext() : false;
	}

	public void removePlace(Vertex vertex) {
		
		this.graph.removeVertex(vertex);
		
		
	}	
	
	
	public Place retrievePlaceByDBPediaId(String dbPediaId) {
		Place place = this.getFramedVertex("dbpediaId", dbPediaId, Place.class);
		
		if(place != null && (place.getType() != null && !place.getType().getType().equals(Type.PLACE))) {
			place = null;
		}
		
		return place;
	}

	public Place retrievePlaceByFreebaseId(String freebaseId) {
		Place place = this.getFramedVertex("freebaseId", freebaseId, Place.class);
		
//		if(place != null && (place.getType() != null && !place.getType().getType().equals(Type.PLACE))) {
//			place = null;
//		}
		
		return place;
	}

	public Place retrievePlaceByGeonamesId(String geonamesId) {
		Place place = this.getFramedVertex("geonamesId", geonamesId, Place.class);
		
		if(place != null && (place.getType() != null  && !place.getType().getType().equals(Type.PLACE))) {
			place = null;
		}
		
		return place;
	}

	public Place retrievePlaceByLinkedGeoDataId(String lgdId) {
		Place place = this.getFramedVertex("lgdId", lgdId, Place.class);
		
//		if(place != null && (place.getType() != null  && !place.getType().getType().equals(Type.PLACE))) {
//			place = null;
//		}
		
		return place;
	}
	
	public Place2NPlaceRel addRelatedNonPlace(Place place, NonPlace nonPlace, String uri) {
		Place2NPlaceRel edge = null;
		double m0 = System.currentTimeMillis();
		Boolean relatedWithNonPLace = nonPlace.isRelatedWithPLace(uri, place.getFreebaseId());
		double m1 = System.currentTimeMillis();
		
		this.getLogger().info("@TIME@IS RELATED:\t" + (m1-m0) + "ms");
		
		if(!relatedWithNonPLace) {
			edge = place.addRelatedEntity(nonPlace);
			edge.setPredicate(uri);
			
		} else {
			this.getLogger().warn("Place NonPlace relationship already defined:\t" + place + "\t" +uri + "\t" + nonPlace);
		}
		
		return edge;
	}
	
}	
