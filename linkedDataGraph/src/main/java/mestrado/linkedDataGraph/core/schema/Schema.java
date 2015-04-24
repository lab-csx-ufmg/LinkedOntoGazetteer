package mestrado.linkedDataGraph.core.schema;

import java.util.Set;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;

public abstract class Schema {

	protected TitanGraph graph;
	
	public Schema(TitanGraph graph) {
		this.graph = graph;
	}
	
	public abstract void create();	
	
	public abstract Set<Class<? extends VertexFrame>> getVertexClasses();
	
	public abstract Set<Class<? extends EdgeFrame>> getEdgeClasses();
}
