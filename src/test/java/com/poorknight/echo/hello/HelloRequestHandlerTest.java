package com.poorknight.echo.hello;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.echo.EchoResponse;

@RunWith(JUnit4.class)
public class HelloRequestHandlerTest {

	private final HelloRequestHandler handler = new HelloRequestHandler();

	@Test
	public void returnsWithCorrectMessage() throws Exception {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech().getText(), equalTo("Hi there."));
	}
}
