package com.poorknight.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Client.class)
public class ThermostatMessagerTest {

	private final static String expectedEndpointOfThermostat = "https://75.38.163.141:35553/tstat";

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	WebResource.Builder builder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private ThermostatMessager messager;

	@Before
	public void setup() {
		initMocks(Client.class);
		PowerMockito.mockStatic(Client.class);
		PowerMockito.when(Client.create()).thenReturn(client);
		when(client.resource(expectedEndpointOfThermostat)).thenReturn(webResource);
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
}