package mestrado.linkedDataGraph.vertex;

import com.tinkerpop.frames.annotations.gremlin.GremlinGroovy;
import com.tinkerpop.frames.annotations.gremlin.GremlinParam;

public interface NonPlace extends Entity {

	@GremlinGroovy(value="it.inE('related').has('predicate', predicate).outV().has('freebaseId', placeId).hasNext()", frame=false)
	public Boolean isRelatedWithPLace(@GremlinParam("predicate") String predicate, @GremlinParam("placeId") String placeId);

}
