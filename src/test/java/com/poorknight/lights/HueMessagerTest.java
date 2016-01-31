package com.poorknight.echo.lights;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.lights.color.LightColor;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Client.class)
public class HueMessagerTest {

	private HueMessager hueMessager;

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	private Builder webResourceBuilder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private final String expectedUrl = "http://162.205.118.185:53335/api/6b1abf1f6e7157cc3843ee8b668d32d/groups/0/action";
	private final String expectedRestType = "application/json";

	@Before
	public void setup() {
		PowerMockito.mockStatic(Client.class);
		when(Client.create()).thenReturn(client);
		when(client.resource(expectedUrl)).thenReturn(webResource);
		when(webResource.type(expectedRestType)).thenReturn(webResourceBuilder);
		hueMessager = new HueMessager();
	}

	@Test
	public void sendsOffRequestWhenCalled() throws Exception {
		hueMessager.sendLightsOffRequest();
		final JsonNode sentRequest = captureSentHueArgument();
		assertThat(sentRequest.get("on").asBoolean(), equalTo(false));
	}

	@Test
	public void sendsOnRequestWhenCalled() throws Exception {
		hueMessager.sendLightsOnRequest();
		final JsonNode sentRequest = captureSentHueArgument();
		assertThat(sentRequest.get("on").asBoolean(), equalTo(true));
	}

	@Test
	public void sendsColorRequestWhenCalled_WithNORMAL() throws Exception {
		hueMessager.sendLightColorRequest(LightColor.NORMAL);

		final JsonNode sentRequest = captureSentHueArgument();
		assertExpectedNonChangingFields(sentRequest);

		assertThat(sentRequest.get("xy").isArray(), equalTo(true));
		assertThat(sentRequest.get("xy").get(0).asDouble(), equalTo(0.4596));
		assertThat(sentRequest.get("xy").get(1).asDouble(), equalTo(0.4105));
	}

	@Test
	public void sendsColorRequestWhenCalled_WithRED() throws Exception {
		hueMessager.sendLightColorRequest(LightColor.RED);

		final JsonNode sentRequest = captureSentHueArgument();
		assertExpectedNonChangingFields(sentRequest);

		assertThat(sentRequest.get("xy").isArray(), equalTo(true));
		assertThat(sentRequest.get("xy").get(0).asDouble(), equalTo(0.5787));
		assertThat(sentRequest.get("xy").get(1).asDouble(), equalTo(0.2694));
	}

	@Test
	public void sendsColorRequestWhenCalled_WithBLUE() throws Exception {
		hueMessager.sendLightColorRequest(LightColor.BLUE);

		final JsonNode sentRequest = captureSentHueArgument();
		assertExpectedNonChangingFields(sentRequest);

		assertThat(sentRequest.get("xy").isArray(), equalTo(true));
		assertThat(sentRequest.get("xy").get(0).asDouble(), equalTo(0.1723));
		assertThat(sentRequest.get("xy").get(1).asDouble(), equalTo(0.0495));
	}

	private void assertExpectedNonChangingFields(final JsonNode sentRequest) {
		assertThat(sentRequest.get("on").asBoolean(), equalTo(true));
		assertThat(sentRequest.get("bri").asInt(), equalTo(254));
		assertThat(sentRequest.get("hue").asInt(), equalTo(14910));
		assertThat(sentRequest.get("sat").asInt(), equalTo(144));
		assertThat(sentRequest.get("ct").asInt(), equalTo(370));
		assertThat(sentRequest.get("effect").asText(), equalTo("none"));
		assertThat(sentRequest.get("alert").asText(), equalTo("none"));
		assertThat(sentRequest.get("colormode").asText(), equalTo("ct"));
	}

	private JsonNode captureSentHueArgument() {
		verify(webResourceBuilder).put(Mockito.eq(ClientResponse.class), captor.capture());
		final JsonNode sentRequest = captor.getValue();
		return sentRequest;
	}
}
