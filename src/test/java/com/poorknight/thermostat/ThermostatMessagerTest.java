package com.poorknight.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.Client;
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

	private final static String thermostatPath = "/tstat";

	@Mock
	private Client client;

	@Mock
	private WebResource.Builder webResource;

	@Mock
	private WebResource.Builder builder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private ThermostatMessager messager;

	@Before
	public void setup() {
		PowerMockito.mockStatic(WebResourceFactory.class);
		PowerMockito.when(WebResourceFactory.buildSecuredHomeWebResource(thermostatPath)).thenReturn(webResource);
		messager = new ThermostatMessager();
	}

	@Test
	public void returnsTempFromThermostat() throws Exception {
		when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(55.5));

		final BigDecimal response = messager.requestCurrentTemp();

		assertThat(response).isEqualTo(new BigDecimal("55.5"));
		verify(webResource).type(MediaType.APPLICATION_JSON_TYPE);
		verify(builder).accept(MediaType.APPLICATION_JSON_TYPE);
		verify(builder).get(JsonNode.class);
	}

	@Test
	public void postsNewDesiredTempToThermostat() throws Exception {
		when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);

		messager.postHeatTargetTemperature(new BigDecimal("68.5"));

		verify(builder).post(eq(JsonNode.class), captor.capture());
		final JsonNode requestBody = captor.getValue();

		assertThat(requestBody.get("t_heat").asDouble()).isEqualTo(68.5);
		assertThat(requestBody.get("tmode").asInt()).isEqualTo(1);
		assertThat(requestBody.get("hold").asInt()).isEqualTo(1);
	}

	@Test
	public void returnsHouseStatusWhenHeaterIsOn() {
		when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(55.5, 1, 73));

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getHouseTemp()).isEqualTo(55.5);
		assertThat(thermostatStatus.getTempSetting()).isEqualTo(73);
		assertThat(thermostatStatus.getFurnaceState()).isEqualTo(ThermostatStatus.FurnaceState.HEAT_ON);
	}

	@Test
	public void returnsHouseStatusWhenHeaterIsOff() {
		when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.get(JsonNode.class)).thenReturn(buildJsonResponseFromThermostat(155.5, 0, 173));

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getHouseTemp()).isEqualTo(155.5);
		assertThat(thermostatStatus.getTempSetting()).isEqualTo(173);
		assertThat(thermostatStatus.getFurnaceState()).isEqualTo(ThermostatStatus.FurnaceState.OFF);
	}

	@Test
	public void houseStatusReadsValuesWithAirConditioningMode() throws IOException {
		when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		JsonNode acJson = new ObjectMapper().readTree("{\n" +
				"  \"fmode\": 0,\n" +
				"  \"fstate\": 1,\n" +
				"  \"hold\": 1,\n" +
				"  \"override\": 1,\n" +
				"  \"t_cool\": 66.0,\n" +
				"  \"t_type_post\": 0,\n" +
				"  \"temp\": 69.5,\n" +
				"  \"time\": {\n" +
				"    \"day\": 5,\n" +
				"    \"hour\": 18,\n" +
				"    \"minute\": 26\n" +
				"  },\n" +
				"  \"tmode\": 2,\n" +
				"  \"tstate\": 2\n" +
				"}");
		when(builder.get(JsonNode.class)).thenReturn(acJson);

		final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();

		assertThat(thermostatStatus.getHouseTemp()).isEqualTo(69.5);
		assertThat(thermostatStatus.getTempSetting()).isEqualTo(66.0);
		assertThat(thermostatStatus.getFurnaceState()).isEqualTo(ThermostatStatus.FurnaceState.AC_ON);
	}

	@Test
	public void throwsExceptionForUnknownThermostatState() throws IOException {
		when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		JsonNode acJson = new ObjectMapper().readTree("{\n" +
				"  \"fmode\": 0,\n" +
				"  \"fstate\": 1,\n" +
				"  \"hold\": 1,\n" +
				"  \"override\": 1,\n" +
				"  \"t_cool\": 66.0,\n" +
				"  \"t_type_post\": 0,\n" +
				"  \"temp\": 69.5,\n" +
				"  \"time\": {\n" +
				"    \"day\": 5,\n" +
				"    \"hour\": 18,\n" +
				"    \"minute\": 26\n" +
				"  },\n" +
				"  \"tmode\": 2,\n" +
				"  \"tstate\": 5\n" +
				"}");
		when(builder.get(JsonNode.class)).thenReturn(acJson);

		try {
			final ThermostatStatus thermostatStatus = messager.requestThermostatStatus();
			fail("expectingException");
		} catch (RuntimeException e) {
			assertThat(e.getMessage()).isEqualTo("Unknown thermostat(tstate) state returned from house thermostat: 5");
		}
	}

	private JsonNode buildJsonResponseFromThermostat(final Double temperature) {
		final ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("temp", temperature);
		response.put("tmode", 1);
		response.put("fmode", 0);
		response.put("override", 1);
		response.put("hold", 1);
		response.put("t_heat", 67);
		response.put("tstate", 0);
		response.put("fstate", 0);

		final ObjectNode timeNode = JsonNodeFactory.instance.objectNode();
		timeNode.put("day", 0);
		timeNode.put("hour", 13);
		timeNode.put("minute", 53);
		response.set("time", timeNode);

		response.put("t_type_post", 0);

		return response;
	}

	private JsonNode buildJsonResponseFromThermostat(double temperature, int tstate, double tempSetting) {
		final ObjectNode response = JsonNodeFactory.instance.objectNode();

		response.put("temp", temperature);
		response.put("tmode", 1);
		response.put("fmode", 0);
		response.put("override", 1);
		response.put("hold", 1);
		response.put("t_heat", tempSetting);
		response.put("tstate", tstate);
		response.put("fstate", 0);

		final ObjectNode timeNode = JsonNodeFactory.instance.objectNode();
		timeNode.put("day", 0);
		timeNode.put("hour", 13);
		timeNode.put("minute", 53);
		response.set("time", timeNode);

		response.put("t_type_post", 0);

		return response;
	}
}