package com.poorknight.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(WebResourceFactory.class)
public class ThermostatMessagerTest {

	private final static String thermostatGetPath = "/house/status";
	private final static String thermostatPostPath = "/tstat";

	@Mock
	private WebResource.Builder getWebResource;

	@Mock
	private WebResource.Builder postWebResource;

	@Mock
	private WebResource.Builder builder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private ThermostatMessager messager;

	@Before
	public void setup() {
		PowerMockito.mockStatic(WebResourceFactory.class);

		PowerMockito.when(WebResourceFactory.buildSecuredHomeWebResource(thermostatGetPath)).thenReturn(getWebResource);
		when(getWebResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);

		PowerMockito.when(WebResourceFactory.buildSecuredHomeWebResource(thermostatPostPath)).thenReturn(postWebResource);
		when(postWebResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);

		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);

		messager = new ThermostatMessager();
	}

	@Test
	public void returnsTempFromThermostat() {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(55.5));

		final BigDecimal response = messager.requestCurrentTemp();

		assertThat(response).isEqualTo(new BigDecimal("55.5"));
		verify(getWebResource).type(MediaType.APPLICATION_JSON_TYPE);
		verify(builder).accept(MediaType.APPLICATION_JSON_TYPE);
		verify(builder).get(JsonNode.class);
	}

	@Test
	public void postsNewDesiredTempToThermostat() {
		messager.postHeatTargetTemperature(new BigDecimal("68.5"));

		verify(builder).post(eq(JsonNode.class), captor.capture());
		final JsonNode requestBody = captor.getValue();

		assertThat(requestBody.get("t_heat").asDouble()).isEqualTo(68.5);
		assertThat(requestBody.get("tmode").asInt()).isEqualTo(1);
		assertThat(requestBody.get("hold").asInt()).isEqualTo(1);
	}

	@Test
	public void returnsHouseStatusWhenHeaterIsOn() {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(55.5, "HEAT_ON", 73, "FURNACE"));

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getHouseTemp()).isEqualTo(55.5);
		assertThat(thermostatStatus.getTempSetting()).isEqualTo(73);
		assertThat(thermostatStatus.getFurnaceState()).isEqualTo(ThermostatStatus.FurnaceState.HEAT_ON);
		assertThat(thermostatStatus.getThermostatMode()).isEqualTo(ThermostatStatus.ThermostatMode.FURNACE_MODE);
	}

	@Test
	public void returnsHouseStatusWhenHeaterIsOff() {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(155.5, "OFF", 173, "FURNACE"));

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getHouseTemp()).isEqualTo(155.5);
		assertThat(thermostatStatus.getTempSetting()).isEqualTo(173);
		assertThat(thermostatStatus.getFurnaceState()).isEqualTo(ThermostatStatus.FurnaceState.OFF);
		assertThat(thermostatStatus.getThermostatMode()).isEqualTo(ThermostatStatus.ThermostatMode.FURNACE_MODE);
	}

	@Test
	public void returnsHouseStatusWhenThermostatModeIsOff() {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(155.5, "OFF", 173, "OFF"));

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getThermostatMode()).isEqualTo(ThermostatStatus.ThermostatMode.OFF);
	}

	@Test
	public void returnsHouseStatusWhenThermostatModeIsAuto() {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(155.5, "HEAT_ON", 173, "AUTO"));

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getThermostatMode()).isEqualTo(ThermostatStatus.ThermostatMode.AUTO_MODE);
	}

	@Test
	public void throwsExceptionForUnknownThermostatMode() {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(155.5, "OFF", 173, "UNKNOWN"));

		try {
			final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();
			fail("expectingException");
		} catch (RuntimeException e) {
			assertThat(e.getMessage()).isEqualTo("Unknown thermostat mode returned from house: UNKNOWN");
		}
	}

	@Test
	public void houseStatusReadsValuesWithAirConditioningMode() throws IOException {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(69.5, "AC_ON", 66.0, "AC"));

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getHouseTemp()).isEqualTo(69.5);
		assertThat(thermostatStatus.getTempSetting()).isEqualTo(66.0);
		assertThat(thermostatStatus.getFurnaceState()).isEqualTo(ThermostatStatus.FurnaceState.AC_ON);
		assertThat(thermostatStatus.getThermostatMode()).isEqualTo(ThermostatStatus.ThermostatMode.AC_MODE);
	}

	@Test
	public void throwsExceptionForUnknownThermostatState() {
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(69.5, "UNKNOWN", 66.0, "AC"));

		try {
			final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();
			fail("expectingException");
		} catch (RuntimeException e) {
			assertThat(e.getMessage()).isEqualTo("Unknown thermostat state returned from house: UNKNOWN");
		}
	}

	private JsonNode buildJsonResponseFromThermostat(final Double temperature) {
		return buildJsonResponseFromThermostat(temperature, "AC_ON", 0.0, "AC");
	}

	private JsonNode buildJsonResponseFromThermostat(double temperature, String state, double tempSetting, String mode) {
		final ObjectNode response = JsonNodeFactory.instance.objectNode();

		response.put("mode", mode);  // OFF, FURNACE, AC, AUTO
		response.put("current_temp", temperature);
		response.put("temp_setting", tempSetting);
		response.put("state", state);  // OFF, HEAT_ON, AC_ON
		response.put("fan_on", true);

		return response;
	}
}