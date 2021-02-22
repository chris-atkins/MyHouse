package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;

public class BedroomOffRequestHandler implements EchoRequestHandler {

	private HouseCommandMessager houseCommandMessager;

	public BedroomOffRequestHandler(HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(HouseCommand.BEDROOM_OFF);
		return EchoResponse.noOutputSpeechResponse();
	}
}
