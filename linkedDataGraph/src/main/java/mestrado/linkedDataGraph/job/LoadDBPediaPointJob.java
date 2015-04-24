package mestrado.linkedDataGraph.job;

import mestrado.linkedDataGraph.core.BatchLoadJob;
import mestrado.linkedDataGraph.vertex.Place;

import org.apache.log4j.Logger;

import com.thinkaurelius.titan.core.attribute.Geoshape;

public class LoadDBPediaPointJob extends BatchLoadJob {

	private static Logger LOGGER = Logger.getLogger(LoadDBPediaPointJob.class);
	
	public LoadDBPediaPointJob() {
		super("dbpedia.point.fpath");
	}

	@Override
	public boolean processLine(String line) {
		String[] split = line.split("\t");

		Place place = this.placeBO.retrievePlaceByDBPediaId(split[0]);

		if (place != null && place.getGeonamesPoint() == null) {
			LoadDBPediaPointJob.LOGGER.info("DBPEDIAPoint:\t" + split[0]);
			place.setDBPediaPoint(Geoshape.point(Double.valueOf(split[1]), Double.valueOf(split[2])));
		}
		return true;
	}

}
