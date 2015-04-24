package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultObject {

	private String name;
	private Long _id;
	private String _type;
	private String dbpediaId;
	private String geonamesId;
	private String freebaseId;

	public Long get_id() {
		return this._id;
	}

	public String get_type() {
		return this._type;
	}

	public String getDbpediaId() {
		return this.dbpediaId;
	}

	public String getGeonamesId() {
		return this.geonamesId;
	}

	public String getFreebaseId() {
		return this.freebaseId;
	}

	public void setFreebaseId(String freebaseId) {
		this.freebaseId = freebaseId;
	}
	
	public String getName() {
		return this.name;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

	public void setDbpediaId(String dbpediaId) {
		this.dbpediaId = dbpediaId;
	}

	public void setGeonamesId(String geonamesId) {
		this.geonamesId = geonamesId;
	}

	public void setName(String name) {
		this.name = name;
	}

}
