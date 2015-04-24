package mestrado.linkedDataGraph.vertex;

import mestrado.linkedDataGraph.edge.EntityType2EntityRel;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public interface EntityType extends VertexFrame {

	public enum Type {PLACE, NONPLACE};
	
	
	@Incidence(label = "type")
	public EntityType2EntityRel addEntity(Entity entity);
	@Adjacency(label = "type", direction = Direction.OUT)
	public Iterable<Entity> getEntities();
	
	
	@Incidence(label = "type")
	public Iterable<EntityType2EntityRel> getEntitiesEdge();
	
	@Property("entityType")
	public Type getType();

	@Property("entityType")
	public void setType(Type type);
	
}
