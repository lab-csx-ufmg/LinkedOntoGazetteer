package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;

public abstract class RESTClient {

	protected static final String BASE_URI = "http://mgeo00.cloudapp.net:8182/graphs/LinkedOntoGazetteer/"; 
	
	protected final HttpHeaders header;

	protected final RestTemplate restTemplate;
	public RESTClient(byte[] encodedAuth) {
		this.restTemplate = new RestTemplate();
		this.header = this.createHeaders(encodedAuth);
	}
	
	public RESTClient(String username, String password) {
		this.restTemplate = new RestTemplate();
		this.header = this.createHeaders(username, password);
	}

	private HttpHeaders createHeaders(final byte[] encodedAuth) {
		return new HttpHeaders() {
			private static final long serialVersionUID = 1L; 
			{
				String authHeader = "Basic " + new String(encodedAuth);
				this.set("Authorization", authHeader);
			}
		};
	}

	private HttpHeaders createHeaders(final String username, final String password) {
		return new HttpHeaders() {
			private static final long serialVersionUID = 1L; 
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				this.set("Authorization", authHeader);
			}
		};
	}

	protected Response exchange(String url, HttpMethod method, Object... uriArgs) {
		ResponseEntity<Response> exchange = this.restTemplate.exchange(url, method, new HttpEntity<Response>(this.header), Response.class, uriArgs);
		return exchange.getBody();
	}
}
