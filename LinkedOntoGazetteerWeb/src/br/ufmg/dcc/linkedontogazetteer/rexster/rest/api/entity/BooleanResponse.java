package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BooleanResponse {

	private String version;
	private Integer totalSize;
	private Double queryTime;
	private Boolean[] results;
	public String getVersion() {
		return this.version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getTotalSize() {
		return this.totalSize;
	}
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}
	public Double getQueryTime() {
		return this.queryTime;
	}
	public void setQueryTime(Double queryTime) {
		this.queryTime = queryTime;
	}
	public Boolean[] getResults() {
		return this.results;
	}
	public void setResults(Boolean[] results) {
		this.results = results;
	}
	
	public Boolean getValue() {
		return this.results[0];
	}
}
