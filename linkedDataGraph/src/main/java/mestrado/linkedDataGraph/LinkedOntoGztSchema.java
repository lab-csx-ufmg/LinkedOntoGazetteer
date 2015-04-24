package mestrado.linkedDataGraph;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import mestrado.linkedDataGraph.core.schema.Schema;
import mestrado.linkedDataGraph.vertex.Entity;
import mestrado.linkedDataGraph.vertex.EntityType;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.log4j.Logger;

import com.thinkaurelius.titan.core.KeyMaker;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public class LinkedOntoGztSchema extends Schema {

	private static Logger LOGGER = Logger.getLogger(LinkedOntoGztSchema.class);
	private static Set<Class<? extends VertexFrame>> VERTEX_SET = new HashSet<Class<? extends VertexFrame>>();
	private static Set<Class<? extends EdgeFrame>> EDGE_SET = new HashSet<Class<? extends EdgeFrame>>();
	
	static {
		LinkedOntoGztSchema.VERTEX_SET.add(EntityType.class);
		LinkedOntoGztSchema.VERTEX_SET.add(Entity.class);
		LinkedOntoGztSchema.VERTEX_SET.add(Name.class);
		LinkedOntoGztSchema.VERTEX_SET.add(Place.class);
	}
	
	public LinkedOntoGztSchema(TitanGraph graph) {
		super(graph);
	}

	@Override
	public void create() {
		
		LinkedOntoGztSchema.LOGGER.debug("Starting create schema");
		for(Class<? extends VertexFrame> clazz : this.getVertexClasses()) {
			this.createIndexes(clazz);
			this.createRelationships(clazz);
		}
		
		for(Class<? extends EdgeFrame> clazz : this.getEdgeClasses()) {
			this.createEdgeProperty(clazz);
		}
		
		this.graph.commit();
	}
	
	private void createEdgeProperty(Class<? extends EdgeFrame> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			Property property = method.getAnnotation(Property.class);
			if (property != null) {
				this.createEdgeProperty(property.value());
			}
		}

	}
	
	private void createEdgeProperty(String indexName) {
		if(this.graph.getType(indexName) == null) {
			LinkedOntoGztSchema.LOGGER.debug("Creating index key: [EDGE PROPERTY] " + indexName);
			this.graph.makeKey(indexName).dataType(String.class).indexed(Edge.class).make();
		} else {
			LinkedOntoGztSchema.LOGGER.debug("Index Key already exists: " + indexName);
		}
	}
	
	/**
	 * 
	 */
	private void createIndexes(Class<? extends VertexFrame> clazz) {
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			Property property = method.getAnnotation(Property.class);
			
			Class<?> propType = null;
			
			
			if (property != null) {

				if(!method.getReturnType().equals(Void.TYPE)) {
					propType = method.getReturnType();
				} else {
					propType = method.getParameterTypes()[0];
				}

				//TODO refatorar para criar de maneira mais dinamica e flexivel os indices
				if(propType.equals(Geoshape.class)) {
					if(this.graph.getType(property.value()) == null) {
						LinkedOntoGztSchema.LOGGER.debug("Creating index key: [SINGLE] [UNIQUE] " + property.value());
						this.graph.makeKey(property.value()).dataType(propType).indexed("search", Vertex.class).single().make();
					} else {
						LinkedOntoGztSchema.LOGGER.debug("Index Key already exists: " + property.value());
					}
				} else {
					if(propType.isEnum()) {
						this.createUniqueSingleIndex(property.value(), String.class);
					} else {
						if(property.value().equals("gnFeatureCode") || property.value().equals("gnFeatureClass")) {
							if(this.graph.getType(property.value()) == null) {
								LinkedOntoGztSchema.LOGGER.debug("Creating index key:" + property.value());
								this.graph.makeKey(property.value()).dataType(propType).indexed(Vertex.class).make();
							} else {
								LinkedOntoGztSchema.LOGGER.debug("Index Key already exists: " + property.value());
							}
						} else {
							this.createUniqueSingleIndex(property.value(), propType);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param edgeLabel
	 */
	private void createManyToManyEdge(String edgeLabel) {
		if(this.graph.getType(edgeLabel) == null) {
			LinkedOntoGztSchema.LOGGER.debug("Creating edge: [MANY TO MANY] " + edgeLabel);
			this.graph.makeLabel(edgeLabel).make();
		} else {
			LinkedOntoGztSchema.LOGGER.debug("Edge already exists: " + edgeLabel);
		}
	}

	/**
	 * 
	 */
	private void createRelationships(Class<? extends VertexFrame> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			Adjacency adjacency = method.getAnnotation(Adjacency.class);
			if (adjacency != null) {
				this.createManyToManyEdge(adjacency.label());
			}
		}
	}
	
	/**
	 * Creates a indexed property that will be unique and single.
	 * @see {@link KeyMaker}
	 * @param indexName
	 * @param propType 
	 */
	private void createUniqueSingleIndex(String indexName, Class<?> propType) {
		if(this.graph.getType(indexName) == null) {
			LinkedOntoGztSchema.LOGGER.debug("Creating index key: [SINGLE] [UNIQUE] " + indexName);
			this.graph.makeKey(indexName).dataType(propType).unique().indexed(Vertex.class).single().make();
		} else {
			LinkedOntoGztSchema.LOGGER.debug("Index Key already exists: " + indexName);
		}
	}

	@Override
	public Set<Class<? extends EdgeFrame>> getEdgeClasses() {
		return LinkedOntoGztSchema.EDGE_SET;
	}

	@Override
	public Set<Class<? extends VertexFrame>> getVertexClasses() {
		return LinkedOntoGztSchema.VERTEX_SET ;
	}
}
