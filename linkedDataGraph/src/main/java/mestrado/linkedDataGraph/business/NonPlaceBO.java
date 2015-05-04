package mestrado.linkedDataGraph.business;

import java.util.Set;

import mestrado.linkedDataGraph.edge.Place2NPlaceRel;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.NonPlace;
import mestrado.linkedDataGraph.vertex.Place;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class NonPlaceBO extends BusinessObject {

	public NonPlaceBO(FramedGraph<TitanGraph> graph) {
		super(graph);
	}

	public NonPlace retrieveNonPlaceByFreebaseId(String freebaseId) {
		NonPlace nonP = this.getFramedVertex("freebaseId", freebaseId, NonPlace.class);
		
		return nonP;
	}

	public NonPlace retrieveNonPlaceByDbPediaId(String dbpediaId) {
		NonPlace nonP = this.getFramedVertex("dbpediaId", dbpediaId, NonPlace.class);
		
		return nonP;
	}
	
	public NonPlace createFreebaseNonPlaceIfNotExists(String freebaseId) {
		NonPlace nonPlaceVertex = this.getFramedVertex("freebaseId", freebaseId, NonPlace.class);
		
		this.getLogger().debug("@createFreebaseNonPlaceIfNotExists: " + nonPlaceVertex);
		
		if(nonPlaceVertex == null) {
			nonPlaceVertex = this.graph.addVertex(null, NonPlace.class);
			nonPlaceVertex.setFreebaseId(freebaseId);
			nonPlaceVertex.setNonPlace(true);
		}
		
		return nonPlaceVertex;
	}

	public NonPlace createDBPediaNonPlaceIfNotExists(String dbpediaId) {
		NonPlace nonPlaceVertex = this.getFramedVertex("dbpediaId", dbpediaId, NonPlace.class);
		
		this.getLogger().debug("@createDbPediaIdNonPlaceIfNotExists: " + nonPlaceVertex);
		
		if(nonPlaceVertex == null) {
			nonPlaceVertex = this.graph.addVertex(null, NonPlace.class);
			nonPlaceVertex.setDbpediaId(dbpediaId);
			nonPlaceVertex.setNonPlace(true);
		}
		
		return nonPlaceVertex;
	}
	
	public void addRelatedPlace(NonPlace nonPlace, Place place, String uri) {
		Set<NonPlace> set = this.iterable2Set(place.getRelatedNonPlaces());
		if(!set.contains(nonPlace)) {
			Place2NPlaceRel edge = place.addRelatedEntity(nonPlace);
			edge.setPredicate(uri);
		} else {
			this.getLogger().warn("Place NonPlace relationship already defined:\t" + place + "\t" +uri + "\t" + nonPlace);
		}
	}
	
	public void addNonPlaceName(NonPlace nonplace, Name name) {
		Set<Name> set = this.iterable2Set(nonplace.getNames());
		if(!set.contains(name)) {
			nonplace.addName(name);
		} else {
			this.getLogger().warn("Non Place name already defined: " + name.getName() + "-->" + nonplace);
		}
		
	}
	
}
