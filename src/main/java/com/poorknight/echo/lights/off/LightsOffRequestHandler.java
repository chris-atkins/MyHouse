package com.poorknight.echo.lights.off;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.lights.HueMessager;

public class LightsOffRequestHandler implements EchoRequestHandler {

	private final HueMessager hueMessager;

	public LightsOffRequestHandler(final HueMessager hueMessager) {
		this.hueMessager = hueMessager;
	}

	@Override
	public EchoResponse handle() {
		hueMessager.sendLightsOffRequest();
		final EchoResponse response = new EchoResponse();
		response.getResponse().setOutputSpeech(null);
		return response;
	}
}
