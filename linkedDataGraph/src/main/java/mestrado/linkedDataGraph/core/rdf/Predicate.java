package mestrado.linkedDataGraph.core.rdf;


public enum Predicate {

	NAME("http://xmlns.com/foaf/0.1/name"),
	NICK("http://xmlns.com/foaf/0.1/nick"),
	COUNTRY("http://dbpedia.org/ontology/country"),
	ISPARTOF("http://dbpedia.org/ontology/isPartOf"),
	LOCATION("http://dbpedia.org/ontology/location"),
	STATE("http://dbpedia.org/ontology/state"),
	REGION("http://dbpedia.org/ontology/region"),
	POINT("http://www.georss.org/georss/point"),
	/*Useless*/
	STATEOFORIGIN(""),
	LOCATIONCITY(""),
	REGIONSERVED(""), 
	LOCATIONCOUNTRY(""),
	COUNTRYORIGIN(""),
	REGIONALLANGUAGE(""),
	ISPARTOFMILITARYCONFLICT(""),
	ISPARTOFANATOMICALSTRUCTURE(""),
	ISPARTOFROUTE(""),
	ISPARTOFWINEREGION(""),
	STATEDELEGATE(""),
	LOCATIONIDENTIFIER(""),
	COUNTRYWITHFIRSTSATELLITE(""),
	COUNTRYWITHFIRSTASTRONAUT(""),
	COUNTRYWITHFIRSTSPACEFLIGHT(""),
	COUNTRYWITHFIRSTSATELLITELAUNCHED("");

	private String uri;
	
	private Predicate(String uri) {
		this.setUri(uri);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
