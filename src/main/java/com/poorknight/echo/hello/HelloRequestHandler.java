package com.poorknight.echo.hello;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;

public class HelloRequestHandler implements EchoRequestHandler {

	@Override
	public EchoResponse handle() {
		return new EchoResponse();
	}

}
