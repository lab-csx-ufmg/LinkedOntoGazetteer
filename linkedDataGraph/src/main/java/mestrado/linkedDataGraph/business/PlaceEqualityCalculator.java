package mestrado.linkedDataGraph.business;

import org.apache.log4j.Logger;

import com.thinkaurelius.titan.core.attribute.Geoshape;

public class PlaceEqualityCalculator {

	private static Logger LOGGER = Logger.getLogger(PlaceEqualityCalculator.class);
	
	public static double calculateEquality(boolean sameCountry, int containdBySamePlacesCount, Geoshape basePlace, Geoshape otherPlace) {
		double probability = 0.0;

		if(basePlace == null) {
			return probability;
		}
		
		if(otherPlace == null) {
			return probability;
		}
		
		if(sameCountry) {
			if(containdBySamePlacesCount > 1) {
				probability = 1.0;
			} else if(containdBySamePlacesCount == 1) {
				probability = 0.5;
			}
			
			
			double distance = basePlace.getPoint().distance(otherPlace.getPoint());
			PlaceEqualityCalculator.LOGGER.info("calculateEquality\t"+sameCountry+"\t"+containdBySamePlacesCount+"\t"+basePlace+"\t"+otherPlace+"\t"+distance);
			
			probability = distance > 5.0 ? 0.0 : probability - (Math.log(1.0 + (distance/5.0)))/2;
			
		}
		
		return probability < 0.0 ? 0.0 : probability;
	}
	
	
}
