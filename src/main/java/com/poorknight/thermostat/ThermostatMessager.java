package com.poorknight.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

public class ThermostatMessager {

	private final WebResource webResource;

	public ThermostatMessager() {
		final Client client = Client.create();
		webResource = client.resource("http://162.205.118.185:35556/tstat");
	}

	public  BigDecimal requestCurrentTemp() {
		final JsonNode response = requestThermostatState();
		return readTempFromResponse(response);
	}

	private JsonNode requestThermostatState() {
		return webResource
					.type(MediaType.APPLICATION_JSON_TYPE)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.get(JsonNode.class);
	}

	private BigDecimal readTempFromResponse(final JsonNode response) {
		return new BigDecimal(response.get("temp").asDouble());
	}
}
