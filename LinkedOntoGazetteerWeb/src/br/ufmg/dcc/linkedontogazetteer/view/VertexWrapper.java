package br.ufmg.dcc.linkedontogazetteer.view;

import org.primefaces.model.map.LatLng;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.ResultObject;

public class VertexWrapper {

	private final Long id;

	private final String uri;

	private String wikipediaURL;
	private String geonamesURL;
	private String freebaseURL;
	private String dbpediaURL;
	
	private LatLng geonamesPoint;
	private LatLng dbpediaPoint;
	private LatLng freebasePoint;
	private LatLng lgdPoint;
	
	private String officialWebsite;
	private String gnFeatureCode;
	private String gnFeatureClass;

	public VertexWrapper(ResultObject resultObject) {
		this.id = resultObject.get_id();
		this.uri = "vertex.xhtml?id=" + resultObject.get_id().toString();

		if (resultObject.getDbpediaId() != null) {
			this.wikipediaURL = "http://en.wikipedia.org/wiki/" + resultObject.getDbpediaId();
			this.dbpediaURL = "http://dbpedia.org/page/" + resultObject.getDbpediaId();
		}

		if (resultObject.getGeonamesId() != null) {
			this.geonamesURL = "http://www.geonames.org/" + resultObject.getGeonamesId();
		}
		
		if(resultObject.getFreebaseId() != null) {
			this.freebaseURL = "http://www.freebase.com/" + resultObject.getFreebaseId().replace(".", "/");
		}
		
		if(resultObject.getDbpPoint() != null) {
			this.dbpediaPoint = this.createPointFromString(resultObject.getDbpPoint());
		}
		
		if(resultObject.getGnPoint() != null) {
			this.geonamesPoint = this.createPointFromString(resultObject.getGnPoint());
		}
		
		if(resultObject.getGnFeatureClass() != null) {
			this.gnFeatureClass = resultObject.getGnFeatureClass();
		}
		
		if(resultObject.getGnFeatureCode() != null) {
			this.gnFeatureCode = resultObject.getGnFeatureCode();
		}
		
		if(resultObject.getOfficial_website() != null) {
			this.officialWebsite = resultObject.getOfficial_website();
		}
		
	}

	public String getGeonamesURL() {
		return this.geonamesURL;
	}

	public Long getId() {
		return this.id;
	}

	public String getUri() {
		return this.uri;
	}

	public String getWikipediaURL() {
		return this.wikipediaURL;
	}

	public void setGeonamesURL(String geonamesURL) {
		this.geonamesURL = geonamesURL;
	}

	public void setWikipediaURL(String wikipediaURL) {
		this.wikipediaURL = wikipediaURL;
	}

	public String getFreebaseURL() {
		return this.freebaseURL;
	}

	public void setFreebaseURL(String freebaseURL) {
		this.freebaseURL = freebaseURL;
	}

	public String getDbpediaURL() {
		return this.dbpediaURL;
	}

	public void setDbpediaURL(String dbpediaURL) {
		this.dbpediaURL = dbpediaURL;
	}

	public LatLng getGeonamesPoint() {
		return this.geonamesPoint;
	}

	public void setGeonamesPoint(LatLng geonamesPoint) {
		this.geonamesPoint = geonamesPoint;
	}

	public LatLng getDbpediaPoint() {
		return this.dbpediaPoint;
	}

	public void setDbpediaPoint(LatLng dbpediaPoint) {
		this.dbpediaPoint = dbpediaPoint;
	}

	public LatLng getFreebasePoint() {
		return this.freebasePoint;
	}

	public void setFreebasePoint(LatLng freebasePoint) {
		this.freebasePoint = freebasePoint;
	}

	public LatLng getLgdPoint() {
		return this.lgdPoint;
	}

	public void setLgdPoint(LatLng lgdPoint) {
		this.lgdPoint = lgdPoint;
	}
	
	private LatLng createPointFromString(String pointStr) {
		String parsedStr = pointStr.replaceAll("point\\[", "").replaceAll("\\]", "");
		String[] latLongArray = parsedStr.split(",");
		
		return new LatLng(Double.valueOf(latLongArray[0]), Double.valueOf(latLongArray[1]));
	}

	public String getOfficialWebsite() {
		return this.officialWebsite;
	}

	public void setOfficialWebsite(String officialWebsite) {
		this.officialWebsite = officialWebsite;
	}

	public String getGnFeatureCode() {
		return this.gnFeatureCode;
	}

	public void setGnFeatureCode(String gnFeatureCode) {
		this.gnFeatureCode = gnFeatureCode;
	}

	public String getGnFeatureClass() {
		return this.gnFeatureClass;
	}

	public void setGnFeatureClass(String gnFeatureClass) {
		this.gnFeatureClass = gnFeatureClass;
	}
}
