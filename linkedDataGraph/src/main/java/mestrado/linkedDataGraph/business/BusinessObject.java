package mestrado.linkedDataGraph.business;

import static com.google.common.collect.Iterables.getOnlyElement;

import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.elasticsearch.common.collect.Sets;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

public class BusinessObject {

	protected FramedGraph<TitanGraph> graph;
	
	public BusinessObject(FramedGraph<TitanGraph> graph) {
		this.graph = graph;
	}
	
	protected <K> Set<K> iterable2Set(Iterable<K> iterable) {
		return Sets.newHashSet(iterable);
	}
	
	public Logger getLogger() {
		return Logger.getLogger(this.getClass());
	}
	
	protected <V extends VertexFrame> V getFramedVertex(String key, String value, Class<V> clazz) {
		V v;
		try {
			v  = getOnlyElement(graph.getVertices(key, value, clazz));
			this.getLogger().debug("@getFramedVertex: Vertex found. [" + key + "] [" + value + "] [" + clazz.getSimpleName() +"]");
		} catch (NoSuchElementException e) {
			this.getLogger().debug("@getFramedVertex: Vertex not found. [" + key + "] [" + value + "] [" + clazz.getSimpleName() +"]");
			v = null;
		} catch (Exception e) {
			e.printStackTrace();
			v = null;
		}
		
		return v;
	}
	
}
