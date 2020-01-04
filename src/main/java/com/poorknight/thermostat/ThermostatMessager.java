package com.poorknight.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.server.WebResourceFactory;
import com.poorknight.thermostat.ThermostatStatus.FurnaceState;
import com.poorknight.thermostat.ThermostatStatus.ThermostatMode;
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
		
		double currentTemp = response.get("temp").asDouble();
		double tempSetting = findTempSetting(response);
		FurnaceState furnaceState = findFurnaceState(response);
		ThermostatMode thermostatMode = findThermostatMode(response);

		return new ThermostatStatus(currentTemp, tempSetting, furnaceState, thermostatMode);
	}

	private double findTempSetting(JsonNode response) {
		JsonNode heatSettingNode = response.get("t_heat");
		if (heatSettingNode != null) {
			return heatSettingNode.asDouble();
		}
		return response.get("t_cool").asDouble();
	}

	private FurnaceState findFurnaceState(JsonNode response) {
		int tstate = response.get("tstate").asInt();
		switch (tstate) {
			case 0: return FurnaceState.OFF;
			case 1: return FurnaceState.HEAT_ON;
			case 2: return FurnaceState.AC_ON;
			default: throw new RuntimeException("Unknown thermostat state (tstate) returned from house thermostat: " + tstate);
		}
	}

	private ThermostatMode findThermostatMode(JsonNode response) {
		int tstate = response.get("tmode").asInt();
		switch (tstate) {
			case 0: return ThermostatMode.OFF;
			case 1: return ThermostatMode.FURNACE_MODE;
			case 2: return ThermostatMode.AC_MODE;
			case 3: return ThermostatMode.AUTO_MODE;
			default: throw new RuntimeException("Unknown thermostat mode (tmode) returned from house thermostat: " + tstate);
		}
	}
}
