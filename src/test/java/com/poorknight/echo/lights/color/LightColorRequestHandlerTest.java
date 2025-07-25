package com.poorknight.echo.lights.color;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.lights.HueMessager;
import com.poorknight.house.lights.LightColor;

public class LightColorRequestHandlerTest {

	private final HueMessager hueMessager = Mockito.mock(HueMessager.class);
	private final DesiredColorTranslator colorTranslator = Mockito.mock(DesiredColorTranslator.class);
	private final JsonNode mockSlots = Mockito.mock(JsonNode.class);
	private LightColorRequestHandler handler;
	private LightColor lightColor;

	@BeforeEach
	public void setup() {
		final JsonNode request = buildRequestWithSlots();
		lightColor = randomLightColor();
		when(colorTranslator.translate(mockSlots)).thenReturn(lightColor);

		handler = new LightColorRequestHandler(request, colorTranslator, hueMessager);
	}

	@Test
	public void sendsLightColorRequestWithColorFromColorTranslator() throws Exception {
		handler.handle();
		verify(hueMessager).sendLightColorRequest(lightColor);
	}

	@Test
	public void returnsResponseWithNoText() {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech(), nullValue());
	}

	private LightColor randomLightColor() {
		return LightColor.values()[RandomUtils.nextInt(0, LightColor.values().length)];
	}

	private JsonNode buildRequestWithSlots() {
		final JsonNodeFactory factory = JsonNodeFactory.instance;
		final ObjectNode root = factory.objectNode();
		final ObjectNode request = factory.objectNode();
		final ObjectNode intent = factory.objectNode();
		final JsonNode slots = mockSlots;

		intent.set("slots", slots);
		request.set("intent", intent);
		root.set("request", request);
		return root;
	}
}
