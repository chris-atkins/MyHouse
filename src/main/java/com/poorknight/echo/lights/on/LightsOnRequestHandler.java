package com.poorknight.echo.lights.on;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.housecommand.HouseCommandMessager;

import static com.poorknight.echo.housecommand.HouseCommand.LIGHTS_ON;

public class LightsOnRequestHandler implements EchoRequestHandler {

	private final HouseCommandMessager houseCommandMessager;

	public LightsOnRequestHandler(final HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(LIGHTS_ON);
		return EchoResponse.noOutputSpeechResponse();
	}
}
