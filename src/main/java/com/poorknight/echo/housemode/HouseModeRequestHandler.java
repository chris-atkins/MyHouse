package com.poorknight.echo.housemode;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

public class HouseModeRequestHandler implements EchoRequestHandler {

	private HouseMode requestedMode;
	private final HouseModeMessager houseModeMessager;

	public HouseModeRequestHandler(final HouseMode requestedMode, final HouseModeMessager houseModeMessager) {
		this.requestedMode = requestedMode;
		this.houseModeMessager = houseModeMessager;
	}

	public HouseMode getRequestedMode() {
		return requestedMode;
	}

	@Override
	public EchoResponse handle() {
		houseModeMessager.requestHouseMode(requestedMode);
		return EchoResponse.responseWithSpeech("Have a good day.");
	}
}
