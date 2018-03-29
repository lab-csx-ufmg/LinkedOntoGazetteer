package br.ufmg.dcc.linkedontogazetteer.rexster.rest.api;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.http.HttpMethod;

import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.BooleanResponse;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.CountResponse;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.PathResponse;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;


public class GremlinRESTClient extends RESTClient {
	// TODO: Remove hardcoded url and set as a env parameter
	// http://sandwich.lbd.dcc.ufmg.br:8182
	protected static final String REQUEST_ENDPOINT = "/tp/gremlin?script=";
	
	public GremlinRESTClient(byte[] encodedAuth, String baseUrl) {
		super(encodedAuth, baseUrl);
	}
	
	public GremlinRESTClient(String username, String password, String url) {
		super(username, password, url);
	}
	
	@Override protected String requestEndpoint() { return REQUEST_ENDPOINT; }
	
	public Response getAllNames(Long vertexId) {
		String queryText = "g.v(%s).in('isName')";
		return this.exchange(getRequestURI() + String.format(queryText, vertexId), HttpMethod.GET, Response.class);
	}
	
	public Response getVerticesByName(String name) {
		String queryText = "g.V('name','%s').out('isName')";
		String formattedQuery = String.format(queryText, StringEscapeUtils.escapeJava(name));
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, Response.class);
	}
	
	public CountResponse countRelatedNonPlaces(Long vertexId) {
		String queryText = "g.v(%s).out('related').count()";
		String formattedQuery = String.format(queryText, vertexId);
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, CountResponse.class);
	}

	public CountResponse countRelatedPlaces(Long vertexId) {
		String queryText = "g.v(%s).both().hasNot('nonplace').count()";
		String formattedQuery = String.format(queryText, vertexId);
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, CountResponse.class);
	}

	public Response getNamesByPlaceId(Long placeId) {
		String queryText = "g.v(%s).in('isName')";
		String formattedQuery = String.format(queryText, placeId);
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, Response.class);
	}

	public Response isPlace(Long id) {
		String queryText = "g.v(%s).hasNot('nonplace')";
		String formattedQuery = String.format(queryText, id);
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, Response.class);
	}

	public Response getPlacesByName(String name) {
		String queryText = "g.V('name','%s').out('isName').hasNot('nonplace')";
		String formattedQuery = String.format(queryText, StringEscapeUtils.escapeJava(name));
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, Response.class);
	}

	public Response getPlacesRelatedWithEntity(String name) {
		String queryText = "g.V('name','%s').out('isName').in('related')";
		String formattedQuery = String.format(queryText, StringEscapeUtils.escapeJava(name));
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, Response.class);
	}

	public PathResponse getPath(Long fromPlaceId, Long toPlaceId, int maxSize) {
		String queryText = "g.v(%s).out('containedBy').loop(1){it.object.id != %s && it.loops < %s}.path";
		String formattedQuery = String.format(queryText, fromPlaceId, toPlaceId, maxSize);
		return this.exchange(getRequestURI() + "{q}", HttpMethod.GET, PathResponse.class, formattedQuery);
	}

	public BooleanResponse isContainedBy(String placeNameA, String placeNameB) {
		String queryText = "g.V('name', '%s').out('isName').as('from').out('containedBy').as('to').in('isName').has('name', '%s').select(['from','to']).dedup().hasNext()";
		String formattedQuery = String.format(queryText, StringEscapeUtils.escapeJava(placeNameA), StringEscapeUtils.escapeJava(placeNameB));
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, BooleanResponse.class);
	}

	public BooleanResponse isContainedBy(Long placeIdA, Long placeIdB) {
		String queryText = "g.v(%s).out('containedBy').filter{it.id == %s}.hasNext()";
		String formattedQuery = String.format(queryText, placeIdA, placeIdB);
		return this.exchange(getRequestURI() + "{q}", HttpMethod.GET, BooleanResponse.class, formattedQuery);
	}

	public String retrievePlaceAdjacentListByName(String name) {
		String queryText = "g.V('name', '%s').out('isName').hasNot('nonplace').groupBy{it}{it.both('containedBy')}{it.findAll{it.hasNot('nonplace')}}.cap";
		String formattedQuery = String.format(queryText, StringEscapeUtils.escapeJava(name));
		return this.exchange(getRequestURI() + "{q}", HttpMethod.GET, String.class, formattedQuery);
	}

	public String retrievePlaceAdjacentListByName(Long id) {
		String queryText = "g.v(%s).hasNot('nonplace').groupBy{it}{it.both('containedBy')}{it.findAll{it.hasNot('nonplace')}}.cap";
		String formattedQuery = String.format(queryText, id);
		return this.exchange(getRequestURI() + "{q}", HttpMethod.GET, String.class, formattedQuery);

	}

	public Response retrieveRelatedEntities(Long placeId) {
		String queryText = "g.v(%s).hasNot('nonplace').out('related')";
		String formattedQuery = String.format(queryText, placeId);
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, Response.class);
	}

	public Response retrieveEntitiesByName(String name) {
		String queryText = "g.V('name','%s').out('isName').has('nonplace')";
		String formattedQuery = String.format(queryText, StringEscapeUtils.escapeJava(name));
		return this.exchange(getRequestURI() + formattedQuery, HttpMethod.GET, Response.class);
	}
}
