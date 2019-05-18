package com.poorknight.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.server.WebResourceFactory;
import com.poorknight.thermostat.ThermostatStatus.FurnaceState;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

public class ThermostatMessager {

	public  BigDecimal requestCurrentTemp() {
		final JsonNode response = requestThermostatState();
		return readTempFromResponse(response);
	}

	private JsonNode requestThermostatState() {
		return prepareJsonRequest().get(JsonNode.class);
	}

	private BigDecimal readTempFromResponse(final JsonNode response) {
		return new BigDecimal(response.get("temp").asDouble());
	}

	public void postHeatTargetTemperature(final BigDecimal targetTemperature) {
		final JsonNode request = buildRequestToSetTempTo(targetTemperature);
		postTempRequest(request);
	}

	private JsonNode buildRequestToSetTempTo(final BigDecimal targetTemperature) {
		final ObjectNode request = JsonNodeFactory.instance.objectNode();
		request.put("t_heat", targetTemperature.doubleValue());
		request.put("tmode", 1);
		request.put("hold", 1);

		return request;
	}

	private JsonNode postTempRequest(final JsonNode request) {
		return prepareJsonRequest().post(JsonNode.class, request);
	}

	private WebResource.Builder prepareJsonRequest() {
		return WebResourceFactory.buildSecuredHomeWebResource("/tstat")
				.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE);
	}

	public ThermostatStatus requestThermostatStatus() {
		final JsonNode response = requestThermostatState();
		System.out.println("Status from thermostat:\n" + response.toString());
		double currentTemp = response.get("temp").asDouble();
		double tempSetting = response.get("t_heat").asDouble();
		double tstate = response.get("tstate").asDouble();
		FurnaceState furnaceState = tstate == 0 ? FurnaceState.OFF : FurnaceState.HEAT_ON;

		return new ThermostatStatus(currentTemp, tempSetting, furnaceState);
	}
}
