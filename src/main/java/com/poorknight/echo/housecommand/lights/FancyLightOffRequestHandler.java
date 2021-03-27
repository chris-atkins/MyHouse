package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;

public class FancyLightOffRequestHandler implements EchoRequestHandler {
	private HouseCommandMessager houseCommandMessager;

	public FancyLightOffRequestHandler(HouseCommandMessager houseCommandMessager) {

		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(HouseCommand.FANCY_LIGHT_OFF);
		return EchoResponse.noOutputSpeechResponse();
	}
}
