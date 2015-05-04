package mestrado.linkedDataGraph.vertex;

import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.annotations.gremlin.GremlinGroovy;
import com.tinkerpop.frames.annotations.gremlin.GremlinParam;

public interface NonPlace extends Entity {

	@Property("nonplace")
	public Boolean getNonPlace();
	
	@Property("nonplace")
	public void setNonPlace(Boolean nonPlace);
	
	@GremlinGroovy(value="it.inE('related').has('predicate', predicate).outV().has('freebaseId', placeId).hasNext()", frame=false)
	public Boolean isRelatedWithPLace(@GremlinParam("predicate") String predicate, @GremlinParam("placeId") String placeId);

}
