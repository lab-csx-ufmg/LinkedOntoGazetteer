package mestrado.linkedDataGraph.vertex;

import mestrado.linkedDataGraph.edge.NPlace2PlaceRel;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public interface Entity extends VertexFrame {

	@Property("nonplace")
	public Boolean getNonPlace();
	
	@Property("nonplace")
	public void setNonPlace(Boolean nonPlace);
	
	@Property("dbpediaId")
	public String getDbpediaId();
	@Property("dbpediaId")
	public void setDbpediaId(String dbpediaId);

	@Property("freebaseId")
	public String getFreebaseId();
	@Property("freebaseId")
	public void setFreebaseId(String freebaseId);
	
	@Property("official_website")
	public String getOfficialWebSite();
	@Property("official_website")
	public void setOfficialWebSite(String officialWebSite);
	
	@Adjacency(label = "related", direction = Direction.OUT)
	public Iterable<Place> getRelatedPlaces();
	
	@Adjacency(label = "type", direction = Direction.IN)
	public EntityType getType();
	
	@Adjacency(label = "isName", direction = Direction.IN)
	public Iterable<Name> getNames();
	@Adjacency(label = "isName", direction = Direction.IN)
	public void addName(Name name);
	
	@Incidence(label = "related")
	public NPlace2PlaceRel addRelatedPlace(Place place);
	
	@Incidence(label = "related")
	public Iterable<NPlace2PlaceRel> getRelatedPlacesEdges();
	
}
