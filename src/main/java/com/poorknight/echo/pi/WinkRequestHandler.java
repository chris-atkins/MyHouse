package com.poorknight.echo.pi;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.pi.PiMessager;

public class WinkRequestHandler implements EchoRequestHandler {

	private final PiMessager piMessager;

	public WinkRequestHandler(final PiMessager piMessager) {
		this.piMessager = piMessager;
	}

	@Override
	public EchoResponse handle() {
		piMessager.sendWinkRequest();
		return EchoResponse.noOutputSpeechResponse();
	}
}
