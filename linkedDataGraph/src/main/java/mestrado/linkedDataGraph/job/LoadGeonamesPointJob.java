package mestrado.linkedDataGraph.job;

import mestrado.linkedDataGraph.core.BatchLoadJob;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.log4j.Logger;

import com.thinkaurelius.titan.core.attribute.Geoshape;

public class LoadGeonamesPointJob extends BatchLoadJob { 

	private static Logger LOGGER = Logger.getLogger(LoadGeonamesPointJob.class);
	
	public LoadGeonamesPointJob() {
		super("geopoints.fpath");
	} 
	
	@Override
	public boolean processLine(String line) {
		String[] split = line.split("\t");
		
		Place place = this.placeBO.retrievePlaceByGeonamesId(split[0]);
		
		if(place != null && place.getGeonamesPoint() == null) {
			LoadGeonamesPointJob.LOGGER.info("Point:\t" + split[0]);
			place.setGeonamesPoint(Geoshape.point(Double.valueOf(split[1]), Double.valueOf(split[2])));
		}
		return true;
	}
}
 