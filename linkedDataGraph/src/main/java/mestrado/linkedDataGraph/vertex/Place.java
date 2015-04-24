package mestrado.linkedDataGraph.vertex;

import mestrado.linkedDataGraph.edge.Place2NPlaceRel;

import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.annotations.gremlin.GremlinGroovy;

public interface Place extends Entity {

	@Adjacency(label = "containedBy", direction = Direction.OUT)
	public void addContainedBy(Place place);
	@Adjacency(label = "containedBy", direction = Direction.IN)
	public void addContains(Place place);

	@Adjacency(label = "country", direction = Direction.OUT)
	public void addCountry(Place place);
	@Incidence(label = "related")
	public Place2NPlaceRel addRelatedEntity(Entity entity);
	
	@GremlinGroovy("it.as('x').in('isName').out('isName').except('x').has('dbpediaId').hasNot('geonamesId')")
    public Iterable<Place> getAmbiguosDbPediaPlaces();
	
	@Adjacency(label = "containedBy", direction = Direction.OUT)
	public Iterable<Place> getContainedBy();
	
	@Adjacency(label = "containedBy", direction = Direction.IN)
	public Iterable<Place> getContains();
	@Adjacency(label = "country", direction = Direction.OUT)
	public Iterable<Place> getCountries();

	@Property("dbpPoint") 
	public Geoshape getDBPediaPoint();

	@Property("setdel")
	public Boolean getDelete();
	
	@Property("frbPoint") 
	public Geoshape getFreebasePoint();
	
	@Property("gnFeatureClass")
	public Character getGeonamesFeatureClass();
	
	@Property("gnFeatureCode")
	public String getGeonamesFeatureCode();
	
	@Property("geonamesId")
	public String getGeonamesId();
	
	@Property("gnPoint") 
	public Geoshape getGeonamesPoint();

	@Property("ontogztId")
	public String getOntogztId();
	
	@Adjacency(label = "related", direction = Direction.OUT)
	public Iterable<Entity> getRelatedEntities();
	
	@Incidence(label = "related")
	public Iterable<Place2NPlaceRel> getRelatedEntitiesEdge();

	@Property("dbpPoint") 
	public void setDBPediaPoint(Geoshape point);

	@Property("setdel")
	public void setDelete(Boolean delete);
	
	@Property("frbPoint") 
	public void setFreebasePoint(Geoshape point);
	
	@Property("gnFeatureClass")
	public void setGeonamesFeatureClass(Character featureClass);
	
	@Property("gnFeatureCode")
	public void setGeonamesFeatureCode(String featureCode);
	
	@Property("geonamesId")
	public void setGeonamesId(String geonamesId);

	@Property("gnPoint") 
	public void setGeonamesPoint(Geoshape point);
	
	@Property("ontogztId")	
	public void setOntogztId(String ontogazId);
	
}
