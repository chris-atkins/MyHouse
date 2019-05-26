package com.poorknight.echo.housecommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

public class HouseCommandMessager {

	public JsonNode requestHouseCommand(final HouseCommand houseCommand) {
		final WebResource.Builder endpoint = buildWebResource();
		final JsonNode requestBody = buildRequestBody(houseCommand);

		return endpoint.put(JsonNode.class, requestBody);
	}

	private WebResource.Builder buildWebResource() {
		return WebResourceFactory.buildSecuredHomeWebResource("/house/command")
					.type(MediaType.APPLICATION_JSON_TYPE)
					.accept(MediaType.APPLICATION_JSON_TYPE);
	}

	private JsonNode buildRequestBody(final HouseCommand houseCommand) {
		return JsonNodeFactory.instance.objectNode().put("command", houseCommand.asPiString());
	}
}
