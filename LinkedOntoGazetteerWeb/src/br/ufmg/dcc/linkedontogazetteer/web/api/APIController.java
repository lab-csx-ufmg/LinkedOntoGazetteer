package br.ufmg.dcc.linkedontogazetteer.web.api;

import java.awt.Point;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ResponseBody
@RestController
public class APIController {

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/isPlace/{id}")
	public String isPlace(@PathVariable Long id) {
		return new String("Não está implementado " + id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/name/place/{placeId}")
	public String retrieveNamesByPlaceId(@PathVariable Long placeId) {
		return new String("Não está implementado " + placeId);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/name/{name}")
	public String retrievePlacesByName(@PathVariable String name) {
		return new String("Não está implementado " + name);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/entity/name/{name}")
	public String retrieveRelatedPlacesByEntityName(@PathVariable String name) {
		return new String("Não está implementado " + name);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/inRectangle/{reference}")
	public String retrievePlacesInRectangle(@RequestParam("ax") int ax, @RequestParam("ay") int ay, @RequestParam("bx") int bx,
			@RequestParam("by") int by, @PathVariable String reference) {
		return new String("Não está implementado " + reference + " - a: " + new Point(ax, ay) + "b: " + new Point(bx, by));
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/path/{fromPlaceId}/{toPlaceId}")
	public String retrievePath(@PathVariable Long fromPlaceId, @PathVariable Long toPlaceId, @RequestParam("maxSize") int maxSize) {
		return new String("Não está implementado " + fromPlaceId + " -> " + toPlaceId + " [" + maxSize + "]");
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/name/containedBy/{placeNameA}/{placeNameB}")
	public String isContainedBy(@PathVariable String placeNameA, @PathVariable String placeNameB) {
		return new String("Não está implementado " + placeNameA + " -- containedBy -> " + placeNameB);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/id/containedBy/{placeIdA}/{placeIdB}")
	public String isContainedBy(@PathVariable Long placeIdA, @PathVariable Long placeIdB) {
		return new String("Não está implementado " + placeIdA + " -- containedBy -> " + placeIdB);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/name/adjacentList/{name}")
	public String retrievePlaceAdjacentListByName(@PathVariable String name) {
		return new String("Não está implementado " + name);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/place/id/adjacentList/{id}")
	public String retrievePlaceAdjacentList(@PathVariable Long id) {
		return new String("Não está implementado " + id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/entity/relatedPlace/{placeId}")
	public String retrieveRelatedEntities(Long placeId) {
		return new String("Não está implementado " + placeId);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/entity/name/{name}")
	public String retrieveAllEntitiesByName(@PathVariable String name) {
		return new String("Não está implementado " + name);
	}
}
