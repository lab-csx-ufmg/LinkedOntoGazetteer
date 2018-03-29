package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class RESTClient {
	// TODO: Remove hardcoded url and set as a env parameter
	// http://sandwich.lbd.dcc.ufmg.br:8182
	protected static final String BASE_ENDPOINT = "/graphs/LinkedOntoGazetteer";
	
	protected final HttpHeaders header;
	protected final String baseUrl;

	protected final RestTemplate restTemplate;
	public RESTClient(byte[] encodedAuth, String baseUrl) {
		this.restTemplate = new RestTemplate();
		this.header = this.createHeaders(encodedAuth);
		this.baseUrl = baseUrl;
	}
	
	public RESTClient(String username, String password, String baseUrl) {
		this.restTemplate = new RestTemplate();
		this.header = this.createHeaders(username, password);
		this.baseUrl = baseUrl;
	}
	
	// singleton
	protected String requestEndpoint() { return ""; }
	
	protected String getRequestURI () {
		return this.baseUrl + BASE_ENDPOINT + requestEndpoint();
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

	protected <T> T exchange(String url, HttpMethod method, Class<T> responseClass, Object... uriArgs) {
		ResponseEntity<T> exchange = this.restTemplate.exchange(url, method, new HttpEntity<T>(this.header), responseClass, uriArgs);
		return exchange.getBody();
	}
}
