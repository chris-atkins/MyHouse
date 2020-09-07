package com.poorknight.echo.lights.off;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommandMessager;

import static com.poorknight.house.commands.HouseCommand.LIGHTS_OFF;

public class LightsOffRequestHandler implements EchoRequestHandler {

	private final HouseCommandMessager houseCommandMessager;

	public LightsOffRequestHandler(final HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(LIGHTS_OFF);
		return EchoResponse.noOutputSpeechResponse();
	}
}
