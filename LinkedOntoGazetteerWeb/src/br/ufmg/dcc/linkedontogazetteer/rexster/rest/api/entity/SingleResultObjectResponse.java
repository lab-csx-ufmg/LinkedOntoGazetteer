package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleResultObjectResponse {

	private String version;
	private Integer totalSize;
	private Double queryTime;
	private ResultObject results;

	public Double getQueryTime() {
		return this.queryTime;
	}

	public ResultObject getResults() {
		return this.results;
	}

	public Integer getTotalSize() {
		return this.totalSize;
	}

	public String getVersion() {
		return this.version;
	}

	public void setQueryTime(Double queryTime) {
		this.queryTime = queryTime;
	}

	public void setResults(ResultObject results) {
		this.results = results;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
