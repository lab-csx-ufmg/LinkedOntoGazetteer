package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api;

import org.springframework.http.HttpMethod;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.SingleResultObjectResponse;

public class VertexClient extends RESTClient {
	protected static final String REQUEST_ENDPOINT = "/vertices";
	
	public VertexClient(String username, String password, String baseUrl) {
		super(username, password, baseUrl);
	}
	
	@Override protected String requestEndpoint() { return REQUEST_ENDPOINT; }
	
	public Response searchByKey(String key, String value) {
		return this.exchange(getRequestURI() + "?key={key}&value={value}", HttpMethod.GET, Response.class, key, value);
	}

	public SingleResultObjectResponse retrieveById(Long placeId) {
		assert(placeId != null);
		return this.exchange(getRequestURI() + "/{placeId}", HttpMethod.GET, SingleResultObjectResponse.class, placeId);
	}
}
