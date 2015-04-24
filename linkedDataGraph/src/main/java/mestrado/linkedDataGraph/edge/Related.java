package mestrado.linkedDataGraph.edge;

import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.Property;

public interface Related extends EdgeFrame{
	
	@Property("predicate")
	public String getPredicate();
	@Property("predicate")
	public void setPredicate(String predicate);

}
