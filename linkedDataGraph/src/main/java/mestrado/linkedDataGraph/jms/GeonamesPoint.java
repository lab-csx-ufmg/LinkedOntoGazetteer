package mestrado.linkedDataGraph.jms;


public class GeonamesPoint {
	private String geonamesId;
	private String latitude;
	private String longitude;

	public GeonamesPoint(String geonamesId, String latitude, String longitude) {
		super();
		this.geonamesId = geonamesId;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getGeonamesId() {
		return this.geonamesId;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setGeonamesId(String geonamesId) {
		this.geonamesId = geonamesId;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return GsonBuilder.getGson().toJson(this);
	}
}
