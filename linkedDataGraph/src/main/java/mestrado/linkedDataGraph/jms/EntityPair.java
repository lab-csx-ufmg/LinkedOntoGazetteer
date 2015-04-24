package mestrado.linkedDataGraph.jms;

public class EntityPair {

	private String geonamesId;
	private String dbpediaId;
	private double probability;
	
	
	public EntityPair(String geonamesId, String dbpediaId, double probability) {
		super();
		this.geonamesId = geonamesId;
		this.dbpediaId = dbpediaId;
		this.probability = probability;
	}
	
	public String getDbpediaId() {
		return this.dbpediaId;
	}
	public String getGeonamesId() {
		return this.geonamesId;
	}
	public double getProbability() {
		return this.probability;
	}
	public void setDbpediaId(String dbpediaId) {
		this.dbpediaId = dbpediaId;
	}
	public void setGeonamesId(String geonamesId) {
		this.geonamesId = geonamesId;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
	

	@Override
	public String toString() {
		return GsonBuilder.getGson().toJson(this);
	}
	
}
