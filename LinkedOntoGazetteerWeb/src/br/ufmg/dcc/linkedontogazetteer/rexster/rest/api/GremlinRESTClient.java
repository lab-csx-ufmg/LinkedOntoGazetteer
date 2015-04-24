package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api;

import org.springframework.http.HttpMethod;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;


public class GremlinRESTClient extends RESTClient {

	protected static final String GREMILIN_BASE_URI = "http://mgeo00.cloudapp.net:8182/graphs/LinkedOntoGazetteer/tp/gremlin?script="; 
	
	public GremlinRESTClient(byte[] encodedAuth) {
		super(encodedAuth);
	}
	
	public GremlinRESTClient(String username, String password) {
		super(username, password);
	}

	public Response getAllNames(Long vertexId) {
		String queryText = "g.v(%s).in('isName')";
		return this.exchange(GremlinRESTClient.GREMILIN_BASE_URI + String.format(queryText, vertexId), HttpMethod.GET, Response.class);
	}
	
	public Response getVerticesByName(String name) {
		String queryText = "g.V('name','%s').out('isName')";
		System.out.println(String.format(queryText, name));
		return this.exchange(GremlinRESTClient.GREMILIN_BASE_URI + String.format(queryText, name), HttpMethod.GET, Response.class);
	}
}
