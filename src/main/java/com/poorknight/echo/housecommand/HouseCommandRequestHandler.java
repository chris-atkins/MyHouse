package com.poorknight.echo.housecommand;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

public class HouseCommandRequestHandler implements EchoRequestHandler {

	private HouseCommand requestedCommand;
	private final HouseCommandMessager houseCommandMessager;
	private final HouseCommandResponseBuilder houseCommandResponseBuilder;

	public HouseCommandRequestHandler(final HouseCommand requestedCommand, final HouseCommandMessager houseCommandMessager, final HouseCommandResponseBuilder houseCommandResponseBuilder) {
		this.requestedCommand = requestedCommand;
		this.houseCommandMessager = houseCommandMessager;
		this.houseCommandResponseBuilder = houseCommandResponseBuilder;
	}

	public HouseCommand getRequestedCommand() {
		return requestedCommand;
	}

	@Override
	public EchoResponse handle() {
		houseCommandMessager.requestHouseCommand(requestedCommand);
		String response = houseCommandResponseBuilder.buildHouseCommandAlexaResponse();
		return EchoResponse.responseWithSpeech(response);
	}
}
