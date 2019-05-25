package com.poorknight.echo.housecommand;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

public class GoingToWorkRequestHandler implements EchoRequestHandler {

	private final HouseCommandMessager houseCommandMessager;
	private final GoingToWorkResponseBuilder goingToWorkResponseBuilder;

	public GoingToWorkRequestHandler(final HouseCommandMessager houseCommandMessager, final GoingToWorkResponseBuilder goingToWorkResponseBuilder) {
		this.houseCommandMessager = houseCommandMessager;
		this.goingToWorkResponseBuilder = goingToWorkResponseBuilder;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(HouseCommand.AT_WORK_MODE);
		String response = goingToWorkResponseBuilder.buildHouseCommandAlexaResponse();
		return EchoResponse.responseWithSpeech(response);
	}
}
