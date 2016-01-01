package com.poorknight.echo;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.hello.HelloRequestHandler;

public class EchoRequestHandlerFactory {

	public static EchoRequestHandler handlerFor(final JsonNode request) {
		final String intentName = request.get("request").get("intent").get("name").asText();

		if (intentName.equals("SayHi")) {
			return new HelloRequestHandler();
		}

		throw new RuntimeException("Unknown intent: " + intentName);
	}

}
