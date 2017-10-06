package com.poorknight.echo.housecommand;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_ON;

public class OutsideLightsOnRequestHandler implements EchoRequestHandler {

	private final HouseCommandMessager houseCommandMessager;

	public OutsideLightsOnRequestHandler(final HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_ON);
		return EchoResponse.noOutputSpeechResponse();
	}
}
