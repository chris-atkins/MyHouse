package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;

public class BedroomOnRequestHandler implements EchoRequestHandler {

	private HouseCommandMessager houseCommandMessager;

	public BedroomOnRequestHandler(HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		this.houseCommandMessager.requestHouseCommand(HouseCommand.BEDROOM_ON);
		return EchoResponse.noOutputSpeechResponse();
	}
}
