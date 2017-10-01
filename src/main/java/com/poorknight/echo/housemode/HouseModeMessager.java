package com.poorknight.echo.housemode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

public class HouseModeMessager {

	/*package*/ void requestHouseMode(final HouseMode houseMode) {
		final WebResource.Builder endpoint = buildWebResource();
		final JsonNode requestBody = buildRequestBody(houseMode);

		endpoint.put(JsonNode.class, requestBody);
	}

	private WebResource.Builder buildWebResource() {
		return WebResourceFactory.buildSecuredHomeWebResource("/house")
					.type(MediaType.APPLICATION_JSON_TYPE)
					.accept(MediaType.APPLICATION_JSON_TYPE);
	}

	private JsonNode buildRequestBody(final HouseMode houseMode) {
		return JsonNodeFactory.instance.objectNode().put("mode", houseMode.getModeAsString());
	}
}
