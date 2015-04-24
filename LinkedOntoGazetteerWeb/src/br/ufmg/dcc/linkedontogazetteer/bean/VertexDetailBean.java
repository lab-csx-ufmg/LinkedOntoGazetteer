package br.ufmg.dcc.linkedontogazetteer.bean;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.springframework.util.StringUtils;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.GremilinRESTClient;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.ResultObject;

@ManagedBean(name = "vertexDetailBean")
public class VertexDetailBean {

	@ManagedProperty(value = "#{param.id}")
	private Long id;
	private GremilinRESTClient client;
	private final SortedSet<String> names = new TreeSet<String>();

	public Long getId() {
		return this.id;
	}

	public SortedSet<String> getNames() {
		return this.names;
	}

	@PostConstruct
	public void init() {
		this.client = new GremilinRESTClient("rexster", "MicroGEO@00");

		Response allNames = this.client.getAllNames(this.id);

		for (ResultObject resultObject : allNames.getResults()) {
			if (!StringUtils.isEmpty(resultObject.getName())) {
				this.names.add(resultObject.getName());
			}
		}
	}

	public void setId(Long id) {
		this.id = id;
	}

}
