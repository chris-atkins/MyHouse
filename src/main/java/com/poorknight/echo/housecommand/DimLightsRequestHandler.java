package com.poorknight.echo.housecommand;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

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
