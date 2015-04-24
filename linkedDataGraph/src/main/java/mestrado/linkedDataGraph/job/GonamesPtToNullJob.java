package mestrado.linkedDataGraph.job;

import mestrado.linkedDataGraph.core.Job;

import com.google.common.collect.Iterables;
import com.tinkerpop.blueprints.Vertex;

public class GonamesPtToNullJob extends Job {

	@Override
	public void execute() {
		
		int geonamesId = 1;
		while(geonamesId < 300000) {
			Iterable<Vertex> vertices = this.graph.getVertices("geonamesId", geonamesId);
			
			if(vertices.iterator().hasNext()) {
				Vertex vertex = Iterables.getOnlyElement(vertices);
				vertex.removeProperty("geonamesPt");
			}
			geonamesId++;
			
			if(geonamesId % 1000 == 0) {
				System.out.println("Commiting...");
				this.graph.getBaseGraph().commit();
			}
			
		}
		
		this.graph.getBaseGraph().commit();
		this.graph.shutdown();
		
	}

}
