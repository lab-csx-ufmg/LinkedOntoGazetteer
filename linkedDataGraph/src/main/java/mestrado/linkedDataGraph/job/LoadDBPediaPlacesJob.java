package mestrado.linkedDataGraph.job;

import mestrado.linkedDataGraph.core.BatchLoadJob;
import mestrado.linkedDataGraph.vertex.EntityType;
import mestrado.linkedDataGraph.vertex.EntityType.Type;
import mestrado.linkedDataGraph.vertex.Place;
public class LoadDBPediaPlacesJob extends BatchLoadJob {

	private final EntityType placeType;
	 
	public LoadDBPediaPlacesJob() {
		super("places.fpath");
		this.placeType = this.graph.addVertex(null, EntityType.class);
		this.placeType.setType(Type.PLACE);
	}
	
	@Override
	public boolean processLine(String line) {
		line = line.replaceAll("[<>]", "");
		String[] split = line.split(" ");
		
		String dbPediaId = split[0].substring("http://dbpedia.org/resource/".length());
		
		Place place = this.graph.addVertex(null, Place.class);
		place.setDbpediaId(dbPediaId);
		
		this.placeType.addEntity(place);

		return true;
	}
}
