package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;

public class DiningLightsBrightRequestHandler implements EchoRequestHandler {

	private HouseCommandMessager houseCommandMessager;

	public DiningLightsBrightRequestHandler(HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(HouseCommand.DINING_LIGHTS_BRIGHT);
		return EchoResponse.noOutputSpeechResponse();
	}
}
