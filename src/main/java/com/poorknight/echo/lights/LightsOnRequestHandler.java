package com.poorknight.echo.lights;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

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
