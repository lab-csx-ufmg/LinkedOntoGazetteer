package mestrado.linkedDataGraph;

import static org.junit.Assert.assertEquals;
import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.frames.FramedGraph;

public class LoadDBPediaPlacesAppTest {

	private static FramedGraph<TitanGraph> graph;

	@BeforeClass
	public static void setup() {
		PropertyConfigurator.configure("test_log4jfilter.properties");
		
    	BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandra");
		config.setProperty("storage.hostname","127.0.0.1");
    	
		LoadDBPediaPlacesAppTest.graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);
	}
	
	@Test
	public void verifyLoad() {
		this.assertID("Angola");
		this.assertID("Yankee_Stadium_(1923)");
		this.assertID("Luis_Mu%C3%B1oz_Mar%C3%ADn_International_Airport");
		this.assertID("Zeh");
	}
	
	public void assertID(String id) {
		Place place = Iterables.getOnlyElement(LoadDBPediaPlacesAppTest.graph.getVertices("dbpediaId", id, Place.class));
		assertEquals(place.getDbpediaId(), id);
		assertEquals(place.getType().getType(), Type.PLACE);
	}
	
}
