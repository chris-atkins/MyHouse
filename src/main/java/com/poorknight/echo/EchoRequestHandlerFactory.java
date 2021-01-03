package com.poorknight.echo;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.hello.HelloRequestHandler;
import com.poorknight.echo.housecommand.combination.GoingToWorkRequestHandler;
import com.poorknight.echo.housecommand.combination.GoingToWorkResponseBuilder;
import com.poorknight.echo.housecommand.lights.*;
import com.poorknight.echo.housecommand.temperature.HouseTempDownHandler;
import com.poorknight.echo.housecommand.temperature.HouseTempUpHandler;
import com.poorknight.echo.housecommand.temperature.TempAdjustmentResponseBuilder;
import com.poorknight.echo.lights.color.DesiredColorTranslator;
import com.poorknight.echo.lights.color.LightColorRequestHandler;
import com.poorknight.echo.pi.WinkRequestHandler;
import com.poorknight.echo.thermostat.HouseTempSettingHandler;
import com.poorknight.echo.thermostat.TempCheckHandler;
import com.poorknight.house.commands.HouseCommandMessager;
import com.poorknight.house.lights.HueMessager;
import com.poorknight.house.pi.PiMessager;
import com.poorknight.house.thermostat.ThermostatMessager;

public class EchoRequestHandlerFactory {

	public static EchoRequestHandler handlerFor(final JsonNode request) {
		final String intentName = findRequestedIntent(request);

		if (intentName.equals("SayHi")) {
			return new HelloRequestHandler();
		}

		if (intentName.equals("LightsOff")) {
			return new LightsOffRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("DimLights")) {
			return new DimLightsRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("LightsOn")) {
			return new LightsOnRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("DiningLightsBright")) {
			return new DiningLightsBrightRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("BasementOff")) {
			return new BasementOffRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("BasementDim")) {
			return new BasementDimRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("BasementOn")) {
			return new BasementOnRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("OutsideLightsOff")) {
			return new OutsideLightsOffRequestHandler(new HouseCommandMessager());
		}

		if (intentName.equals("OutsideLightsOn")) {
			return new OutsideLightsOnRequestHandler(new HouseCommandMessager());
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

		if (intentName.equals("HouseTempSetting")) {
			return new HouseTempSettingHandler(new ThermostatMessager());
		}

		if(intentName.equals("HouseTempUp")) {
			return new HouseTempUpHandler(new HouseCommandMessager(), new TempAdjustmentResponseBuilder());
		}

		if(intentName.equals("HouseTempDown")) {
			return new HouseTempDownHandler(new HouseCommandMessager(), new TempAdjustmentResponseBuilder());
		}

		if(intentName.equals("AtWorkMode")) {
			return new GoingToWorkRequestHandler(new HouseCommandMessager(), new GoingToWorkResponseBuilder());
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