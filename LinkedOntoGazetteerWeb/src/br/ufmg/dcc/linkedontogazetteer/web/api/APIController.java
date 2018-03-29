package br.ufmg.dcc.linkedontogazetteer.web.api;

import java.awt.Point;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.ufmg.dcc.linkedontogazetteer.AppConfiguration;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.GremlinRESTClient;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.BooleanResponse;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.PathResponse;
import br.ufmg.dcc.linkedontogazetteer.rexster.rest.api.entity.Response;

@ResponseBody
@RestController
public class APIController {
	private static final AppConfiguration config = AppConfiguration.getConfiguration();
	private final GremlinRESTClient client = new GremlinRESTClient(APIController.config.getRexsterUser(), APIController.config.getRexsterPassword(), APIController.config.getRexsterHost());
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/isPlace/{id}")
	public Response isPlace(@PathVariable Long id) {
		return this.client.isPlace(id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/name/place/{placeId}")
	public Response retrieveNamesByPlaceId(@PathVariable Long placeId) {
		return this.client.getNamesByPlaceId(placeId);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/name/{name}")
	public Response retrievePlacesByName(@PathVariable String name) {
		return this.client.getPlacesByName(name);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/entity/name/{name}")
	public Response retrieveRelatedPlacesByEntityName(@PathVariable String name) {
		return this.client.getPlacesRelatedWithEntity(name);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/inRectangle/{reference}")
	public String retrievePlacesInRectangle(@RequestParam("ax") int ax, @RequestParam("ay") int ay, @RequestParam("bx") int bx,
			@RequestParam("by") int by, @PathVariable String reference) {
		return new String("N�o est� implementado " + reference + " - a: " + new Point(ax, ay) + "b: " + new Point(bx, by));
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/path/{fromPlaceId}/{toPlaceId}")
	public PathResponse retrievePath(@PathVariable Long fromPlaceId, @PathVariable Long toPlaceId, @RequestParam("maxSize") int maxSize) {
		return this.client.getPath(fromPlaceId, toPlaceId, maxSize);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/name/containedBy/{placeNameA}/{placeNameB}")
	public BooleanResponse isContainedBy(@PathVariable String placeNameA, @PathVariable String placeNameB) {
		return this.client.isContainedBy(placeNameA, placeNameB);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/id/containedBy/{placeIdA}/{placeIdB}")
	public BooleanResponse isContainedBy(@PathVariable Long placeIdA, @PathVariable Long placeIdB) {
		return this.client.isContainedBy(placeIdA, placeIdB);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/name/adjacentList/{name}")
	public String retrievePlaceAdjacentListByName(@PathVariable String name) {
		return this.client.retrievePlaceAdjacentListByName(name);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/id/adjacentList/{id}")
	public String retrievePlaceAdjacentList(@PathVariable Long id) {
		return this.client.retrievePlaceAdjacentListByName(id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/entity/relatedPlace/{placeId}")
	public Response retrieveRelatedEntities(Long placeId) {
		return this.client.retrieveRelatedEntities(placeId);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/entity/name/{name}")
	public Response retrieveAllEntitiesByName(@PathVariable String name) {
		return this.client.retrieveEntitiesByName(name);
	}
}
