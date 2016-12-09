package com.poorknight.lights;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class HueMessager {

	private final Client client;
	private final WebResource webResource;
	private final JsonNodeFactory nodeFactory;

	public HueMessager() {
		client = Client.create();
		webResource = client.resource("https://75.38.163.141:35553/lights/state");
		nodeFactory = JsonNodeFactory.instance;
	}

	public void sendLightsOffRequest() {
		sendLightsRequestWithOn(false);
	}

	public void sendLightsOnRequest() {
		sendLightsRequestWithOn(true);
	}

	private ClientResponse sendLightsRequestWithOn(final boolean lightsOn) {
		final ObjectNode data = nodeFactory.objectNode();
		data.set("on", JsonNodeFactory.instance.booleanNode(lightsOn));

		final ClientResponse response = webResource.type("application/json").put(ClientResponse.class, data);
		return response;
	}

	public ClientResponse sendLightColorRequest(final LightColor lightColor) {
		final ObjectNode data = nodeFactory.objectNode();
		data.set("on", nodeFactory.booleanNode(true));
		data.set("bri", nodeFactory.numberNode(254));
		data.set("hue", nodeFactory.numberNode(15630));
		data.set("sat", nodeFactory.numberNode(105));
		data.set("ct", nodeFactory.numberNode(330));
		data.set("effect", nodeFactory.textNode("none"));
		data.set("alert", nodeFactory.textNode("none"));
		data.set("colormode", nodeFactory.textNode("ct"));
		data.set("xy", buildArrayNodeForColor(lightColor));

		final ClientResponse response = webResource.type("application/json").put(ClientResponse.class, data);
		return response;
	}

	private JsonNode buildArrayNodeForColor(final LightColor lightColor) {
		switch (lightColor) {
		case BLUE:
			return nodeFactory.arrayNode().add(0.1723).add(0.0495);
		case RED:
			return nodeFactory.arrayNode().add(0.5787).add(0.2694);
		case NORMAL:
			return nodeFactory.arrayNode().add(0.435).add(0.4036);
		default:
			throw new RuntimeException("No color setup in HueMessager for " + lightColor.name());
		}
	}
}
