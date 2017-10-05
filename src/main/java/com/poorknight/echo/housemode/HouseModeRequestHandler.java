package com.poorknight.echo.housemode;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

public class HouseModeRequestHandler implements EchoRequestHandler {

	private HouseMode requestedMode;
	private final HouseModeMessager houseModeMessager;
	private final HouseModeResponseBuilder houseModeResponseBuilder;

	public HouseModeRequestHandler(final HouseMode requestedMode, final HouseModeMessager houseModeMessager, final HouseModeResponseBuilder houseModeResponseBuilder) {
		this.requestedMode = requestedMode;
		this.houseModeMessager = houseModeMessager;
		this.houseModeResponseBuilder = houseModeResponseBuilder;
	}

	public HouseMode getRequestedMode() {
		return requestedMode;
	}

	@Override
	public EchoResponse handle() {
		houseModeMessager.requestHouseMode(requestedMode);
		String response = houseModeResponseBuilder.buildHouseModeAlexaResponse();
		return EchoResponse.responseWithSpeech(response);
	}
}
