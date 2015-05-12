package br.ufmg.dcc.linkedontogazetteer.bean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.util.StringUtils;

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
		final GremlinRESTClient client = this.client;
		Collections.sort(this.results, new Comparator<VertexWrapper>() {
			
			private Double linkCount(VertexWrapper v) {
				System.out.println("loading vertex info: " + v.getId());
				double count = 0.0;
				if(v.getDbpediaURL() != null) {
					count=+2;
				}
				if(v.getFreebaseURL() != null) {
					count++;
				}
				
//				if(count > 0 && !v.isRelatedNonPlacesUpdated()) {
//					CountResponse response = client.countRelatedNonPlaces(v.getId());
//					v.setRelatedNonPlacesCount(response.getCount());
//				}
				
//				if(!v.isRelatedPlacesUpdated()) {
//					CountResponse response = client.countRelatedPlaces(v.getId());
//					v.setRelatedPlacesCount(response.getCount());
//				}
				
				if(v.getGeonamesURL() != null) {
					count++;
				}
				if(v.getOfficialWebsite() != null) {
					count++;
				}
//				count+=v.getRelatedPlacesCount();
				
				if(v.getNames().isEmpty()) {
					Response allNames = client.getAllNames(v.getId());
					
					for (ResultObject resultObject : allNames.getResults()) {
						if (!StringUtils.isEmpty(resultObject.getName())) {
							v.getNames().add(resultObject.getName());
						}
					}
				}
				double rank = count + Math.min(v.getNames().size()/10.0, 3.0);
				System.out.println("[Done] loading vertex info: " + v.getId() + "\t" + rank);
				return rank;
			}
			
			@Override
			public int compare(VertexWrapper o1, VertexWrapper o2) {
				return (-1) * this.linkCount(o1).compareTo(this.linkCount(o2));
			}
		});
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