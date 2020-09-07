package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommandMessager;

import static com.poorknight.house.commands.HouseCommand.OUTSIDE_LIGHTS_OFF;

public class OutsideLightsOffRequestHandler implements EchoRequestHandler {

	private final HouseCommandMessager houseCommandMessager;

	public OutsideLightsOffRequestHandler(final HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_OFF);
		return EchoResponse.noOutputSpeechResponse();
	}
}
