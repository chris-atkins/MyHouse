package com.poorknight.echo;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.hello.HelloRequestHandler;
import com.poorknight.echo.housemode.HouseModeMessager;
import com.poorknight.echo.housemode.HouseModeRequestHandler;
import com.poorknight.echo.housemode.HouseModeResponseBuilder;
import com.poorknight.echo.lights.color.DesiredColorTranslator;
import com.poorknight.echo.lights.color.LightColorRequestHandler;
import com.poorknight.echo.lights.off.LightsOffRequestHandler;
import com.poorknight.echo.lights.on.LightsOnRequestHandler;
import com.poorknight.echo.pi.WinkRequestHandler;
import com.poorknight.echo.thermostat.HeatOffHandler;
import com.poorknight.echo.thermostat.HeatOnHandler;
import com.poorknight.echo.thermostat.TempCheckHandler;
import com.poorknight.lights.HueMessager;
import com.poorknight.pi.PiMessager;
import com.poorknight.thermostat.ThermostatMessager;

import static com.poorknight.echo.housemode.HouseMode.AT_WORK;

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

		if (intentName.equals("Wink")) {
			return new WinkRequestHandler(new PiMessager());
		}

		if (intentName.equals("TempCheck")) {
			return new TempCheckHandler(new ThermostatMessager());
		}

		if(intentName.equals("HeatOn")) {
			return new HeatOnHandler(new ThermostatMessager());
		}

		if(intentName.equals("HeatOff")) {
			return new HeatOffHandler(new ThermostatMessager());
		}

		if(intentName.equals("AtWorkMode")) {
			return new HouseModeRequestHandler(AT_WORK, new HouseModeMessager(), new HouseModeResponseBuilder());
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