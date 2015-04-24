package mestrado.linkedDataGraph.jms;

public class GeonamesDetails {

	private String geonamesId;
	private char featureClass;
	private String featureCode;

	public GeonamesDetails(String geonamesId, Character featureClass, String featureCode) {
		if(geonamesId == null || featureClass == null || featureCode == null) {
			throw new NullPointerException("Unable to instantiate GeonamesDetails: " + geonamesId + "," + featureClass + ", " + featureCode);
		}
		this.geonamesId = geonamesId;
		this.featureClass = featureClass;
		this.featureCode = featureCode;
	}

	public char getFeatureClass() {
		return this.featureClass;
	}
	public String getFeatureCode() {
		return this.featureCode;
	}
	public String getGeonamesId() {
		return this.geonamesId;
	}
	public void setFeatureClass(char featureClass) {
		this.featureClass = featureClass;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}
	public void setGeonamesId(String geonamesId) {
		this.geonamesId = geonamesId;
	}
	
	@Override
	public String toString() {
		return GsonBuilder.getGson().toJson(this);
	}
}
