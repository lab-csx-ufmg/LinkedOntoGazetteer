package mestrado.linkedDataGraph.edge;

import mestrado.linkedDataGraph.vertex.NonPlace;
import mestrado.linkedDataGraph.vertex.Place;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Place2NPlaceRel extends Related {

	@OutVertex
	public Place getPlace();
	
	@InVertex
	public NonPlace getNonPlace();
	
}
