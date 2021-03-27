package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;

public class FancyLightOnRequestHandler implements EchoRequestHandler {

	private HouseCommandMessager houseCommandMessager;

	public FancyLightOnRequestHandler(HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		this.houseCommandMessager.requestHouseCommand(HouseCommand.FANCY_LIGHT_ON);
		return EchoResponse.noOutputSpeechResponse();
	}
}
