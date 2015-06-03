package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountResponse {

	private String version;
	private Integer totalSize;
	private Double queryTime;
	private Integer[] results;
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
	public Integer[] getResults() {
		return this.results;
	}
	public void setResults(Integer[] results) {
		this.results = results;
	}
	
	public int getCount() {
		return this.results[0];
	}
}
