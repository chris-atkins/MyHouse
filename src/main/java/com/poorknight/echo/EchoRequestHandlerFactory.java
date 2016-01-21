package com.poorknight.echo;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.hello.HelloRequestHandler;
import com.poorknight.echo.lights.HueMessager;
import com.poorknight.echo.lights.color.DesiredColorTranslator;
import com.poorknight.echo.lights.color.LightColorRequestHandler;
import com.poorknight.echo.lights.off.LightsOffRequestHandler;
import com.poorknight.echo.lights.on.LightsOnRequestHandler;

public class EchoRequestHandlerFactory {

	public static EchoRequestHandler handlerFor(final JsonNode request) {
		final String intentName = findRequestedIntent(request);

		if (intentName.equals("SayHi")) {
			return new HelloRequestHandler();
		}

		if (intentName.equals("LightsOff")) {
			return new LightsOffRequestHandler(new HueMessager());
		}

		if (intentName.equals("LightsOn")) {
			return new LightsOnRequestHandler(new HueMessager());
		}

		if (intentName.equals("LightColor")) {
			return new LightColorRequestHandler(request, new DesiredColorTranslator(), new HueMessager());
		}

		throw new RuntimeException("Unknown intent: " + intentName);
	}

	private static String findRequestedIntent(final JsonNode request) {
		try {
			return request.get("request").get("intent").get("name").asText();

		} catch (final Exception e) {
			throw new RuntimeException("Cannot find intent in request:\n" + request.toString(), e);
		}
	}
}