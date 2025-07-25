package com.poorknight.house.lights;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
public class HueMessagerTest {

	private HueMessager hueMessager = new HueMessager();

	@Mock
	private WebResource.Builder webResource;

	@Mock
	private WebResource.Builder webResourceBuilder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private final String lightsPath = "/lights/state";
	private final String expectedRestType = "application/json";

	@BeforeEach
	public void setup() {
		when(webResource.type(expectedRestType)).thenReturn(webResourceBuilder);
	}

	@Test
	public void sendsOffRequestWhenCalled() {
		try(MockedStatic<WebResourceFactory> mockedFactory = mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(lightsPath)).thenReturn(webResource);

			hueMessager.sendLightsOffRequest();
			final JsonNode sentRequest = captureSentHueArgument();
			assertThat(sentRequest.get("on").asBoolean(), equalTo(false));
		}
	}

	@Test
	public void sendsOnRequestWhenCalled_WithSameArgumentsAsNORMAL() {
		try(MockedStatic<WebResourceFactory> mockedFactory = mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(lightsPath)).thenReturn(webResource);

			hueMessager.sendLightsOnRequest();
			final JsonNode sentRequest = captureSentHueArgument();
			assertExpectedNonChangingFields(sentRequest);

			assertThat(sentRequest.get("xy").isArray(), equalTo(true));
			assertThat(sentRequest.get("xy").get(0).asDouble(), equalTo(0.3852));
			assertThat(sentRequest.get("xy").get(1).asDouble(), equalTo(0.3815));
		}
	}

	@Test
	public void sendsColorRequestWhenCalled_WithNORMAL() {
		try(MockedStatic<WebResourceFactory> mockedFactory = mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(lightsPath)).thenReturn(webResource);

			hueMessager.sendLightColorRequest(LightColor.NORMAL);

			final JsonNode sentRequest = captureSentHueArgument();
			assertExpectedNonChangingFields(sentRequest);

			assertThat(sentRequest.get("xy").isArray(), equalTo(true));
			assertThat(sentRequest.get("xy").get(0).asDouble(), equalTo(0.3852));
			assertThat(sentRequest.get("xy").get(1).asDouble(), equalTo(0.3815));
		}
	}

	@Test
	public void sendsColorRequestWhenCalled_WithRED() {
		try(MockedStatic<WebResourceFactory> mockedFactory = mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(lightsPath)).thenReturn(webResource);

			hueMessager.sendLightColorRequest(LightColor.RED);

			final JsonNode sentRequest = captureSentHueArgument();
			assertExpectedNonChangingFields(sentRequest);

			assertThat(sentRequest.get("xy").isArray(), equalTo(true));
			assertThat(sentRequest.get("xy").get(0).asDouble(), equalTo(0.5787));
			assertThat(sentRequest.get("xy").get(1).asDouble(), equalTo(0.2694));
		}
	}

	@Test
	public void sendsColorRequestWhenCalled_WithBLUE() {
		try(MockedStatic<WebResourceFactory> mockedFactory = mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(lightsPath)).thenReturn(webResource);

			hueMessager.sendLightColorRequest(LightColor.BLUE);

			final JsonNode sentRequest = captureSentHueArgument();
			assertExpectedNonChangingFields(sentRequest);

			assertThat(sentRequest.get("xy").isArray(), equalTo(true));
			assertThat(sentRequest.get("xy").get(0).asDouble(), equalTo(0.1723));
			assertThat(sentRequest.get("xy").get(1).asDouble(), equalTo(0.0495));
		}
	}

	private void assertExpectedNonChangingFields(final JsonNode sentRequest) {
		assertThat(sentRequest.get("on").asBoolean(), equalTo(true));
		assertThat(sentRequest.get("bri").asInt(), equalTo(254));
		assertThat(sentRequest.get("hue").asInt(), equalTo(19228));
		assertThat(sentRequest.get("sat").asInt(), equalTo(13));
		assertThat(sentRequest.get("ct").asInt(), equalTo(257));
		assertThat(sentRequest.get("effect").asText(), equalTo("none"));
		assertThat(sentRequest.get("alert").asText(), equalTo("none"));
		assertThat(sentRequest.get("colormode"), nullValue());
	}

	private JsonNode captureSentHueArgument() {
		verify(webResourceBuilder).put(Mockito.eq(ClientResponse.class), captor.capture());
		final JsonNode sentRequest = captor.getValue();
		return sentRequest;
	}
}
