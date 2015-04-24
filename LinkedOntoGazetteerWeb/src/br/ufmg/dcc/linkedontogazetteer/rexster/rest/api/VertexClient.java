package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api;

import org.springframework.http.HttpMethod;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;

public class VertexClient extends RESTClient {

	private static final String KEY_VALUE_ENDPOINT = "vertices?key={key}&value={value}";
	
	public VertexClient(String username, String password) {
		super(username, password);
	}
	
	public Response searchByKey(String key, String value) {
		return this.exchange(RESTClient.BASE_URI + VertexClient.KEY_VALUE_ENDPOINT, HttpMethod.GET, key, value);
	}
	
}
