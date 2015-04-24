package mestrado.linkedDataGraph.edge;

import mestrado.linkedDataGraph.vertex.Entity;
import mestrado.linkedDataGraph.vertex.Place;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface NPlace2PlaceRel extends Related {

	@InVertex
	public Place getPlace();
	
	@OutVertex
	public Entity getNonPlace();
	
}
