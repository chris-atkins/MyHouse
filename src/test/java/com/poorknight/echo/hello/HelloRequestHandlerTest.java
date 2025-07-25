package com.poorknight.echo.hello;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.poorknight.echo.EchoResponse;

public class HelloRequestHandlerTest {

	private final HelloRequestHandler handler = new HelloRequestHandler();

	@Test
	public void returnsWithCorrectMessage() throws Exception {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech().getText(), equalTo("Hi there."));
	}
}
