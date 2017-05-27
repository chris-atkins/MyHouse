package com.poorknight.lights;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class HueMessager {

	private final JsonNodeFactory nodeFactory;

	public HueMessager() {
		nodeFactory = JsonNodeFactory.instance;
	}


	public void sendLightsOffRequest() {
		sendLightsRequestWithOn(false);
	}

	public void sendLightsOnRequest() {
		sendLightColorRequest(LightColor.NORMAL);
	}

	private ClientResponse sendLightsRequestWithOn(final boolean lightsOn) {
		final ObjectNode data = nodeFactory.objectNode();
		data.set("on", JsonNodeFactory.instance.booleanNode(lightsOn));

		final ClientResponse response = prepareJsonRequest().put(ClientResponse.class, data);
		return response;
	}

	public ClientResponse sendLightColorRequest(final LightColor lightColor) {
		final ObjectNode data = nodeFactory.objectNode();
		data.set("on", nodeFactory.booleanNode(true));
		data.set("bri", nodeFactory.numberNode(254));
		data.set("hue", nodeFactory.numberNode(19228));
		data.set("sat", nodeFactory.numberNode(13));
		data.set("ct", nodeFactory.numberNode(257));
		data.set("effect", nodeFactory.textNode("none"));
		data.set("alert", nodeFactory.textNode("none"));
		data.set("colormode", nodeFactory.textNode("ct"));
		data.set("xy", buildArrayNodeForColor(lightColor));

		final ClientResponse response = prepareJsonRequest().put(ClientResponse.class, data);
		return response;
	}

	private WebResource.Builder prepareJsonRequest() {
		return WebResourceFactory.buildSecuredHomeWebResource("/lights/state").type("application/json");
	}

	private JsonNode buildArrayNodeForColor(final LightColor lightColor) {
		switch (lightColor) {
		case BLUE:
			return nodeFactory.arrayNode().add(0.1723).add(0.0495);
		case RED:
			return nodeFactory.arrayNode().add(0.5787).add(0.2694);
		case NORMAL:
			return nodeFactory.arrayNode().add(0.3852).add(0.3815);
		default:
			throw new RuntimeException("No color setup in HueMessager for " + lightColor.name());
		}
	}
}
