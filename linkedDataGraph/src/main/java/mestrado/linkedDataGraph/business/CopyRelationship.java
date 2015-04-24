package mestrado.linkedDataGraph.business;

import java.util.Iterator;

import com.tinkerpop.frames.VertexFrame;


public abstract class CopyRelationship<F extends VertexFrame, T extends VertexFrame> {

	public abstract void copy(T to, F object);
	
	public void copyFrom(T to, Iterable<F> it) {
		Iterator<F> iterator = it.iterator();
		while(iterator.hasNext()) {
			this.copy(to, iterator.next());
		}
	}
	
}
