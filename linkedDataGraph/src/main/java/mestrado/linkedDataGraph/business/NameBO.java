package mestrado.linkedDataGraph.business;

import mestrado.linkedDataGraph.vertex.Name;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class NameBO extends BusinessObject {

	public NameBO(FramedGraph<TitanGraph> graph) {
		super(graph);
	}

	/**
	 * Check if the graph contains a vertex of type Name with the key name;
	 * @param name
	 * @return True if exists a vertex with a name key with the value name;
	 */
	public boolean containsName(String name) {
		return this.graph.getVertices("name", name).iterator().hasNext();
	}
	
	/**
	 * Check if exists a vertex with a name key value equals name. If exists return that, otherwise creates a new one;
	 * @param name
	 * @return A Name vertex V, where V.name = name.
	 */
	public Name createNameIfNotExists(String name) {
		Name nameVertex = this.getVertexByName(name);
		
		this.getLogger().debug("@createNameIfNotExists: " + nameVertex);
		
		if(nameVertex == null) {
			nameVertex = graph.addVertex(null, Name.class);
			nameVertex.setName(name);
		}
		
		return nameVertex;
	}
	
	/**
	 * Return a vertex V where V.name = name, if not exists a vertex that match this clause return null.
	 * @param name
	 * @return A vertex V where V.name = name, if not exists a vertex that match this clause return null.
	 */
	public Name getVertexByName(String name) {
		return this.getFramedVertex("name", name, Name.class);
	}
}
