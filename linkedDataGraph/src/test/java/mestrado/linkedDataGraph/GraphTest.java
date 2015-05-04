package mestrado.linkedDataGraph;

import java.util.Iterator;

import mestrado.linkedDataGraph.core.schema.SchemaGraphFactory;
import mestrado.linkedDataGraph.edge.EntityType2EntityRel;
import mestrado.linkedDataGraph.vertex.Entity;
import mestrado.linkedDataGraph.vertex.EntityType;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Name;
import mestrado.linkedDataGraph.vertex.NonPlace;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;

public class GraphTest {

	private static FramedGraph<TitanGraph> graph;
	
	private static Place brasil;
	private static Place mg;
	private static Place bh;
	private static Place esmeralda;
	
	private static Place paris;
	private static Place france;
	
	private static Place usa;
	private static Place texas;
	private static Place paris_tx;
	
	private static Name nameBrasil;
	private static Name nameMG;
	private static Name nameBH;
	private static Name nameParis;
	private static Name nameFrance;
	private static Name nameUSA;
	private static Name nameTexas;
	private static Name nameEsmeralda;
	
	
	private static EntityType placeType;
	private static EntityType nonPlaceType;
	
	private static NonPlace dilma;
	private static NonPlace obama;
	private static NonPlace santosDrummond;
	private static NonPlace esmeraldaStone;

	
	private static void doEntityPropTest(Entity entity, String label) {
		Assert.assertNull(entity.getDbpediaId());
        Assert.assertNull(entity.getFreebaseId());

        entity.setDbpediaId(label);
        entity.setFreebaseId(label);
        
        Vertex vertex = GraphTest.graph.getVertex(entity.asVertex().getId());
        
        Assert.assertEquals(vertex.getProperty("dbpediaId"), entity.getDbpediaId());
        Assert.assertEquals(vertex.getProperty("freebaseId"), entity.getFreebaseId());
	}
	
	private static void doPlacePropTest(Place place, String label) {
		Assert.assertNull(place.getDbpediaId());
		Assert.assertNull(place.getFreebaseId());
		Assert.assertNull(place.getOntogztId());
		Assert.assertNull(place.getGeonamesId());
		
		place.setDbpediaId(label);
		place.setFreebaseId(label);
		place.setOntogztId(label);
		place.setGeonamesId(label);
		
		Vertex vertex = GraphTest.graph.getVertex(place.asVertex().getId());
		
		Assert.assertEquals(vertex.getProperty("dbpediaId"), place.getDbpediaId());
		Assert.assertEquals(vertex.getProperty("freebaseId"), place.getFreebaseId());
		Assert.assertEquals(vertex.getProperty("ontogztId"), place.getOntogztId());
		Assert.assertEquals(vertex.getProperty("geonamesId"), place.getGeonamesId());
	}

	@BeforeClass
	public static void setup() {
		PropertyConfigurator.configure("test_log4jfilter.properties");
		
    	BaseConfiguration config = new BaseConfiguration();
    	config.setProperty("storage.backend","cassandrathrift");
		config.setProperty("storage.hostname","192.168.0.119");
		config.setProperty("storage.index.search.backend","elasticsearch");
		config.setProperty("storage.index.search.directory","../db/es");
		config.setProperty("storage.index.search.client-only",false);
		config.setProperty("storage.index.search.local-mode",true);

    	
		GraphTest.graph = SchemaGraphFactory.open(LinkedOntoGztSchema.class, config);

		GraphTest.dilma = GraphTest.graph.addVertex(null, NonPlace.class);
		GraphTest.obama = GraphTest.graph.addVertex(null, NonPlace.class);
		GraphTest.santosDrummond = GraphTest.graph.addVertex(null, NonPlace.class);
		GraphTest.esmeraldaStone = GraphTest.graph.addVertex(null, NonPlace.class);
		
		GraphTest.brasil = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.mg = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.bh = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.esmeralda = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.usa = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.texas = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.paris_tx = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.france = GraphTest.graph.addVertex(null, Place.class);
		GraphTest.paris = GraphTest.graph.addVertex(null, Place.class);
		
		GraphTest.nameBrasil = GraphTest.graph.addVertex(null, Name.class);
		GraphTest.nameMG = GraphTest.graph.addVertex(null, Name.class);
		GraphTest.nameBH = GraphTest.graph.addVertex(null, Name.class);
		GraphTest.nameEsmeralda = GraphTest.graph.addVertex(null, Name.class);
		GraphTest.nameParis = GraphTest.graph.addVertex(null, Name.class);
		GraphTest.nameFrance = GraphTest.graph.addVertex(null, Name.class);
		GraphTest.nameUSA = GraphTest.graph.addVertex(null, Name.class);
		GraphTest.nameTexas = GraphTest.graph.addVertex(null, Name.class);
		
		GraphTest.placeType = Iterables.getOnlyElement(GraphTest.graph.getVertices("entityType", EntityType.Type.PLACE, EntityType.class));
		
		GraphTest.nonPlaceType = GraphTest.graph.addVertex(null, EntityType.class);
		GraphTest.nonPlaceType.setType(Type.NONPLACE);
		
		GraphTest.doEntityPropTest(GraphTest.dilma, "dilma");
		GraphTest.doEntityPropTest(GraphTest.esmeraldaStone, "Esmeralda_Stone");
		GraphTest.doEntityPropTest(GraphTest.obama, "obama");
		GraphTest.doEntityPropTest(GraphTest.santosDrummond, "santosDrummond");
		
		GraphTest.doPlacePropTest(GraphTest.brasil, "Brazil");
		GraphTest.doPlacePropTest(GraphTest.mg, "Minas_Gerais");
		GraphTest.doPlacePropTest(GraphTest.bh, "Belo_Horizonte");
		GraphTest.doPlacePropTest(GraphTest.esmeralda, "Esmeralda_MG");
		GraphTest.doPlacePropTest(GraphTest.usa, "USA");
		GraphTest.doPlacePropTest(GraphTest.texas, "Texas");
		GraphTest.doPlacePropTest(GraphTest.paris_tx, "ParisTX");
		GraphTest.doPlacePropTest(GraphTest.france, "France");
		GraphTest.doPlacePropTest(GraphTest.paris, "Paris");

		GraphTest.nameBrasil.setName("Brasil");
		GraphTest.nameBrasil.addEntity(GraphTest.brasil);
		
		GraphTest.nameMG.setName("Minas Gerais");
		GraphTest.nameMG.addEntity(GraphTest.mg);
		
		GraphTest.nameBH.setName("Belo Horizonte");
		GraphTest.nameBH.addEntity(GraphTest.bh);
		
		GraphTest.nameEsmeralda.setName("Esmeralda");
		GraphTest.nameEsmeralda.addEntity(GraphTest.esmeralda);
		GraphTest.nameEsmeralda.addEntity(GraphTest.esmeraldaStone);
		
		GraphTest.nameParis.setName("Paris");
		GraphTest.nameParis.addEntity(GraphTest.paris);
		GraphTest.nameParis.addEntity(GraphTest.paris_tx);
		
		GraphTest.nameFrance.setName("França");
		GraphTest.nameFrance.addEntity(GraphTest.france);
		
		GraphTest.nameUSA.setName("Estados Unidos");
		GraphTest.nameUSA.addEntity(GraphTest.usa);
		
		GraphTest.nameTexas.setName("Texas");
		GraphTest.nameTexas.addEntity(GraphTest.texas);
		
		GraphTest.placeType.addEntity(GraphTest.bh);
		GraphTest.placeType.addEntity(GraphTest.brasil);
		GraphTest.placeType.addEntity(GraphTest.mg);
		GraphTest.placeType.addEntity(GraphTest.esmeralda);
		GraphTest.placeType.addEntity(GraphTest.usa);
		GraphTest.placeType.addEntity(GraphTest.texas);
		GraphTest.placeType.addEntity(GraphTest.paris);
		GraphTest.placeType.addEntity(GraphTest.paris_tx);
		GraphTest.placeType.addEntity(GraphTest.france);
		
		
		EntityType2EntityRel edge = GraphTest.nonPlaceType.addEntity(GraphTest.dilma);
		edge.setPredicate("Person");
		
		edge = GraphTest.nonPlaceType.addEntity(GraphTest.obama);
		edge.setPredicate("Person");
		
		edge = GraphTest.nonPlaceType.addEntity(GraphTest.esmeraldaStone);
		edge.setPredicate("Stone");
		
		edge = GraphTest.nonPlaceType.addEntity(GraphTest.santosDrummond);
		edge.setPredicate("Person");
		
		
		GraphTest.bh.addContainedBy(GraphTest.mg);
		GraphTest.bh.addContainedBy(GraphTest.brasil);
		GraphTest.bh.addCountry(GraphTest.brasil);
		GraphTest.bh.addRelatedEntity(GraphTest.dilma);

		GraphTest.esmeralda.addContainedBy(GraphTest.mg);
		GraphTest.esmeralda.addContainedBy(GraphTest.brasil);
		GraphTest.esmeralda.addCountry(GraphTest.brasil);

		GraphTest.mg.addContainedBy(GraphTest.brasil);
		GraphTest.mg.addCountry(GraphTest.brasil);
		
		GraphTest.brasil.addRelatedEntity(GraphTest.dilma);
		GraphTest.brasil.addRelatedEntity(GraphTest.santosDrummond);
		
		
		GraphTest.paris.addContainedBy(GraphTest.france);
		GraphTest.paris.addCountry(GraphTest.france);
		
		GraphTest.france.addRelatedEntity(GraphTest.santosDrummond);
		
		GraphTest.paris_tx.addContainedBy(GraphTest.texas);
		GraphTest.paris_tx.addContainedBy(GraphTest.usa);
		GraphTest.paris_tx.addCountry(GraphTest.usa);
		
		GraphTest.texas.addContainedBy(GraphTest.usa);
		GraphTest.texas.addCountry(GraphTest.usa);
		
		GraphTest.usa.addRelatedEntity(GraphTest.obama);
		
		GraphTest.dilma.addRelatedPlace(GraphTest.bh);
		GraphTest.dilma.addRelatedPlace(GraphTest.brasil);
		GraphTest.obama.addRelatedPlace(GraphTest.usa);
		GraphTest.santosDrummond.addRelatedPlace(GraphTest.brasil);
		GraphTest.santosDrummond.addRelatedPlace(GraphTest.france);

	}
	
	@Test
	public void bhRelationshipsTest() {
		
		Name nameV = Iterables.getOnlyElement(GraphTest.graph.getVertices("name", "Belo Horizonte", Name.class));
		
		Vertex asVertex = Iterables.getOnlyElement(nameV.getEntities()).asVertex();
		
		Place bh2 = GraphTest.graph.getVertex(asVertex.getId(), Place.class);
		
		Assert.assertTrue(Iterables.contains(bh2.getContainedBy(), GraphTest.mg));
		Assert.assertTrue(Iterables.contains(bh2.getContainedBy(), GraphTest.brasil));
		Assert.assertTrue(Iterables.contains(bh2.getRelatedNonPlaces(), GraphTest.dilma));
		
				
		
	}

	@Test
	public void defineTypeTest() {
		Iterable<Entity> entities1 = GraphTest.placeType.getEntities();
		Iterable<Entity> entities2 = Iterables.getOnlyElement(GraphTest.graph.getVertices("entityType", Type.PLACE, EntityType.class)).getEntities();

		Assert.assertTrue(Iterables.elementsEqual(entities1, entities2));
	
	}
	
	@Test
	public void edgePropertiesTest() {
		EntityType et = Iterables.getOnlyElement(GraphTest.graph.getVertices("entityType", Type.NONPLACE, EntityType.class));
		
		Iterator<EntityType2EntityRel> iterator = et.getEntitiesEdge().iterator();
		
		while(iterator.hasNext()) {
			String predicate = iterator.next().getPredicate();
			Assert.assertTrue(predicate.equals("Person") || predicate.equals("Stone"));
		}
	}
	
	@Test
	public void esmeraldaAmbiguityTest() {
		Name esmeraldaV = Iterables.getOnlyElement(GraphTest.graph.getVertices("name", "Esmeralda", Name.class));
		
		Iterator<Entity> iterator = esmeraldaV.getEntities().iterator();
		int count = 0 ;
		while(iterator.hasNext()) {
			count++;
			iterator.next();
		}
		
		Assert.assertEquals(2, count);
	}
	
	@Test
	public void placeEqualityTest() {
		Assert.assertEquals(GraphTest.bh, Iterables.getOnlyElement(GraphTest.graph.getVertices("dbpediaId", "Belo_Horizonte", Place.class)));
		Assert.assertNotEquals(GraphTest.paris, Iterables.getOnlyElement(GraphTest.graph.getVertices("dbpediaId", "Belo_Horizonte", Place.class)));
	}
	

}
