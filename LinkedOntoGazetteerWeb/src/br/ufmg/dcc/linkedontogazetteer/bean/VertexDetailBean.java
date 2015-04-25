package br.ufmg.dcc.linkedontogazetteer.bean;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

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
	
	public Long getId() {
		return this.id;
	}

	public SortedSet<String> getNames() {
		return this.names;
	}

	@PostConstruct
	public void init() {
		this.gremlinClient = new GremlinRESTClient(VertexDetailBean.config.getRexsterUser(), VertexDetailBean.config.getRexsterPassword());
		this.vertexClient = new VertexClient(VertexDetailBean.config.getRexsterUser(), VertexDetailBean.config.getRexsterPassword());
		
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
}
