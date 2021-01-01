package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;

public class BasementOnRequestHandler implements EchoRequestHandler {


	private HouseCommandMessager houseCommandMessager;

	public BasementOnRequestHandler(HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(HouseCommand.BASEMENT_ON);
		return EchoResponse.noOutputSpeechResponse();
	}
}
