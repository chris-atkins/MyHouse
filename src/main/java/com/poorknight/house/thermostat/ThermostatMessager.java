package com.poorknight.house.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.server.WebResourceFactory;
import com.poorknight.house.thermostat.ThermostatStatus.FurnaceState;
import com.poorknight.house.thermostat.ThermostatStatus.ThermostatMode;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

public class ThermostatMessager {

	private static final String getEndpoint = "/house/status";
	private static final String postEndpoint = "/thermostat/state";


	public void setHeatModeOnWithTargetTemp(final BigDecimal targetTemperature) {
		final JsonNode request = buildRequestToSetTempTo(targetTemperature);
		postTempRequest(request);
	}

	private JsonNode buildRequestToSetTempTo(final BigDecimal targetTemperature) {
		final ObjectNode request = JsonNodeFactory.instance.objectNode();
		request.put("targetTemp", targetTemperature.doubleValue());
		request.put("mode", "HEAT");

		return request;
	}

	private JsonNode postTempRequest(final JsonNode request) {
		return preparePostJsonRequest().post(JsonNode.class, request);
	}

	private WebResource.Builder preparePostJsonRequest() {
		return WebResourceFactory.buildSecuredHomeWebResource(postEndpoint)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE);
	}

	/////////////////////////////////////////////////////////////////////////////////////

	public  BigDecimal requestCurrentTemp() {
		final ThermostatStatus status = requestThermostatStatus();
		return new BigDecimal(status.getHouseTemp());
	}

	public ThermostatStatus requestThermostatStatus() {
		final JsonNode response = requestThermostatState();

		double currentTemp = response.get("current_temp").asDouble();
		double tempSetting = response.get("temp_setting").asDouble();
		FurnaceState furnaceState = findFurnaceState(response);
		ThermostatMode thermostatMode = findThermostatMode(response);

		return new ThermostatStatus(currentTemp, tempSetting, furnaceState, thermostatMode);
	}

	private JsonNode requestThermostatState() {
		return prepareGetJsonRequest().get(JsonNode.class);
	}

	private WebResource.Builder prepareGetJsonRequest() {
		return WebResourceFactory.buildSecuredHomeWebResource(getEndpoint)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE);
	}

	private FurnaceState findFurnaceState(JsonNode response) {
		String state = response.get("state").asText();
		switch (state) {
			case "OFF": return FurnaceState.OFF;
			case "HEAT_ON": return FurnaceState.HEAT_ON;
			case "AC_ON": return FurnaceState.AC_ON;
			default: throw new RuntimeException("Unknown thermostat state returned from house: " + state);
		}
	}

	private ThermostatMode findThermostatMode(JsonNode response) {
		String mode = response.get("mode").asText();
		switch (mode) {
			case "OFF": return ThermostatMode.OFF;
			case "FURNACE": return ThermostatMode.FURNACE_MODE;
			case "AC": return ThermostatMode.AC_MODE;
			case "AUTO": return ThermostatMode.AUTO_MODE;
			default: throw new RuntimeException("Unknown thermostat mode returned from house: " + mode);
		}
	}
}
