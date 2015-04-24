package mestrado.linkedDataGraph.core.schema;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.configuration.BaseConfiguration;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;

public class SchemaGraphFactory extends TitanFactory {

	private static final FramedGraphFactory framedGraphfactory = new FramedGraphFactory(new GremlinGroovyModule());
	
	 public static <T extends Schema> FramedGraph<TitanGraph> open(Class<T> clazz, BaseConfiguration config) {
		TitanGraph graph = TitanFactory.open(config);
		T schema = null;
		
		try {																																																																																																																																																																																																																																																 
			schema = clazz.getConstructor(TitanGraph.class).newInstance(graph);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		schema.create();
		
		return SchemaGraphFactory.framedGraphfactory.create(graph);
	}
	
}
