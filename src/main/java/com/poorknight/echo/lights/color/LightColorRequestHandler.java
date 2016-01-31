package com.poorknight.echo.lights.color;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.lights.HueMessager;

public class LightColorRequestHandler implements EchoRequestHandler {

	private final DesiredColorTranslator colorTranslator;
	private final JsonNode request;
	private final HueMessager hueMessager;

	public LightColorRequestHandler(final JsonNode request, final DesiredColorTranslator colorTranslator, final HueMessager hueMessager) {
		this.request = request;
		this.colorTranslator = colorTranslator;
		this.hueMessager = hueMessager;
	}

	@Override
	public EchoResponse handle() {
		final JsonNode slots = request.get("request").get("intent").get("slots");
		hueMessager.sendLightColorRequest(colorTranslator.translate(slots));

		final EchoResponse response = new EchoResponse();
		response.getResponse().setOutputSpeech(null);
		return response;
	}
}
