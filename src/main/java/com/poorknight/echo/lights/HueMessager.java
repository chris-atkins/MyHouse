package com.poorknight.echo.lights;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class HueMessager {

	public void sendLightsOffRequest() {
		sendLightsRequestWithOn(false);
	}

	public void sendLightsOnRequest() {
		sendLightsRequestWithOn(true);
	}

	private ClientResponse sendLightsRequestWithOn(final boolean lightsOn) {
		final Client client = Client.create();
		final WebResource webResource = client.resource("http://162.205.118.185:53335/api/6b1abf1f6e7157cc3843ee8b668d32d/groups/0/action");
		final ObjectNode data = JsonNodeFactory.instance.objectNode();
		data.set("on", JsonNodeFactory.instance.booleanNode(lightsOn));
		final ClientResponse response = webResource.type("application/json").put(ClientResponse.class, data);
		return response;
	}
}
