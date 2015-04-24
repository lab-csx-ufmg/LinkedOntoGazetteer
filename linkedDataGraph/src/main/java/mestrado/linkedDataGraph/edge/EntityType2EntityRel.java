package mestrado.linkedDataGraph.edge;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

import mestrado.linkedDataGraph.vertex.EntityType;
import mestrado.linkedDataGraph.vertex.Entity;

public interface EntityType2EntityRel extends Related {

	@InVertex
	public Entity getEntity();
	
	@OutVertex
	public EntityType getType();
	
}
