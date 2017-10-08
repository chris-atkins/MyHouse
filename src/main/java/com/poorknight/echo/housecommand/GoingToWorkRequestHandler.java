package com.poorknight.echo.housecommand;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

public class GoingToWorkRequestHandler implements EchoRequestHandler {

	private final HouseCommandMessager houseCommandMessager;
	private final HouseCommandResponseBuilder houseCommandResponseBuilder;

	public GoingToWorkRequestHandler(final HouseCommandMessager houseCommandMessager, final HouseCommandResponseBuilder houseCommandResponseBuilder) {
		this.houseCommandMessager = houseCommandMessager;
		this.houseCommandResponseBuilder = houseCommandResponseBuilder;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(HouseCommand.AT_WORK_MODE);
		String response = houseCommandResponseBuilder.buildHouseCommandAlexaResponse();
		return EchoResponse.responseWithSpeech(response);
	}
}
