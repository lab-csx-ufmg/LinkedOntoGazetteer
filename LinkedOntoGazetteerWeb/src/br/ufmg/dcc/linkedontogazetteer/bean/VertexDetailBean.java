package br.ufmg.dcc.linkedontogazetteer.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.map.Marker;
import org.springframework.util.StringUtils;

import br.ufmg.dcc.linkedontogazetteer.AppConfiguration;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.GremlinRESTClient;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.VertexClient;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.ResultObject;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.SingleResultObjectResponse;
import br.ufmg.dcc.linkedontogazetteer.view.VertexWrapper;

@ManagedBean(name = "vertexDetailBean")
public class VertexDetailBean {
	private static final AppConfiguration config = AppConfiguration.getConfiguration();
	
	@ManagedProperty(value = "#{param.id}")
	private Long id;
	
	private GremlinRESTClient gremlinClient;
	private final SortedSet<String> names = new TreeSet<String>();
	private VertexClient vertexClient;

	private VertexWrapper vertex;
	
	private GMapModelBean mapModelBean; 
	
	public Long getId() {
		return this.id;
	}

	public SortedSet<String> getNames() {
		return this.names;
	}

	@PostConstruct
	public void init() {
		this.gremlinClient = new GremlinRESTClient(VertexDetailBean.config.getRexsterUser(), VertexDetailBean.config.getRexsterPassword(), VertexDetailBean.config.getRexsterHost());
		this.vertexClient = new VertexClient(VertexDetailBean.config.getRexsterUser(), VertexDetailBean.config.getRexsterPassword(), VertexDetailBean.config.getRexsterHost());
		
		Response allNames = this.gremlinClient.getAllNames(this.id);

		for (ResultObject resultObject : allNames.getResults()) {
			if (!StringUtils.isEmpty(resultObject.getName())) {
				this.names.add(resultObject.getName());
			}
		}
		
		SingleResultObjectResponse retrieveById = this.vertexClient.retrieveById(this.id);
		
		if(retrieveById.getResults() != null) {
			this.vertex = new VertexWrapper(retrieveById.getResults());
		}
		
		List<Marker> markers = new ArrayList<Marker>();
		
		if(this.vertex.getDbpediaPoint() != null) {
			markers.add(new Marker(this.vertex.getDbpediaPoint(), "DBPedia"));
		}

		if(this.vertex.getGeonamesPoint() != null) {
			markers.add(new Marker(this.vertex.getGeonamesPoint(), "GeoNames"));
		}

		if(this.vertex.getFreebasePoint() != null) {
			markers.add(new Marker(this.vertex.getFreebasePoint(), "Freebase"));
		}
		
		this.mapModelBean = new GMapModelBean(markers);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VertexWrapper getVertex() {
		return this.vertex;
	}

	public void setVertex(VertexWrapper vertex) {
		this.vertex = vertex;
	}

	public GMapModelBean getMapModelBean() {
		return this.mapModelBean;
	}

	public void setMapModelBean(GMapModelBean mapModelBean) {
		this.mapModelBean = mapModelBean;
	}
}
