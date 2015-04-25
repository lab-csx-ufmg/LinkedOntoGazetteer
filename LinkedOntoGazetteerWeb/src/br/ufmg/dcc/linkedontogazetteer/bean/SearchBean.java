package br.ufmg.dcc.linkedontogazetteer.bean;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.ufmg.dcc.linkedontogazetteer.AppConfiguration;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.GremlinRESTClient;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.ResultObject;
import br.ufmg.dcc.linkedontogazetteer.view.VertexWrapper;
 
@ManagedBean(name = "searchBean")
public class SearchBean {
	private static final AppConfiguration config = AppConfiguration.getConfiguration();
	
	private String queryText;
	private Double queryTime;
	
	private final GremlinRESTClient client;
	private ArrayList<VertexWrapper> results = new ArrayList<VertexWrapper>();
	
	
	public SearchBean() {
		this.client = new GremlinRESTClient(SearchBean.config.getRexsterUser(), SearchBean.config.getRexsterPassword());
	}
	
	public void doQuery() {
		if(this.queryText == null || this.queryText.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please, query a place name	",  null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
		
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