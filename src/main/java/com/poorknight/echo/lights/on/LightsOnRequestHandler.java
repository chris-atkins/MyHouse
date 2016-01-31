package com.poorknight.echo.lights.on;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.lights.HueMessager;

public class LightsOnRequestHandler implements EchoRequestHandler {

	private final HueMessager hueMessager;

	public LightsOnRequestHandler(final HueMessager hueMessager) {
		this.hueMessager = hueMessager;
	}

	@Override
	public EchoResponse handle() {
		hueMessager.sendLightsOnRequest();
		final EchoResponse response = new EchoResponse();
		response.getResponse().setOutputSpeech(null);
		return response;
	}
}
