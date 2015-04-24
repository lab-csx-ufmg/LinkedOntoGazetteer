package mestrado.linkedDataGraph.vertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public interface Name extends VertexFrame {

	@Property("name")
	public String getName();
	@Property("name")
	public void setName(String name);
	
	
	@Adjacency(label = "isName", direction = Direction.OUT)
	public Iterable<Entity> getEntities();
	@Adjacency(label="isName", direction = Direction.OUT)
	public void addEntity(Entity entity);
	
}
