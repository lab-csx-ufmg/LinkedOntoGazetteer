package br.ufmg.dcc.linkedontogazetteer.bean;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.GremlinRESTClient;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.ResultObject;
import br.ufmg.dcc.linkedontogazetteer.view.VertexWrapper;
 
@ManagedBean(name = "searchBean")
public class SearchBean {
 
	private String queryText;
	private Double queryTime;
	
	private final GremlinRESTClient client;
	private ArrayList<VertexWrapper> results = new ArrayList<VertexWrapper>();
	
	
	public SearchBean() {
		this.client = new GremlinRESTClient("rexster", "MicroGEO@00");
	}
	
	public void doQuery() {
		Response response = this.client.getVerticesByName(this.queryText);
		this.queryTime = response.getQueryTime();
		System.out.println(response.getQueryTime());
		this.results.clear();
		
		for(ResultObject robj : response.getResults()) {
			this.results.add(new VertexWrapper(robj));
		}
	}

	public String getQueryText() {
		return this.queryText;
	}

	public Double getQueryTime() {
		return this.queryTime;
	}


	public ArrayList<VertexWrapper> getResults() {
		return this.results;
	}


	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public void setQueryTime(Double queryTime) {
		this.queryTime = queryTime;
	}

	public void setResults(ArrayList<VertexWrapper> results) {
		this.results = results;
	}
	
}