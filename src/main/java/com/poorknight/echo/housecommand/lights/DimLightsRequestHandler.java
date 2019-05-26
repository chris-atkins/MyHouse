package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.housecommand.HouseCommand;
import com.poorknight.echo.housecommand.HouseCommandMessager;

public class DimLightsRequestHandler implements EchoRequestHandler {


	private HouseCommandMessager houseCommandMessager;

	public DimLightsRequestHandler(HouseCommandMessager houseCommandMessager) {
		this.houseCommandMessager = houseCommandMessager;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(HouseCommand.DIM_LIGHTS);
		return EchoResponse.noOutputSpeechResponse();
	}
}
