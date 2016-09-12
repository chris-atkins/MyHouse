package com.poorknight.echo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class EchoResponseTest {

	@Test
	public void buildsCorrectNoOutputSpeechResponse() throws Exception {
		final EchoResponse echoResponse = EchoResponse.noOutputSpeechResponse();
		assertThat(echoResponse.getVersion(), equalTo("1.0"));
		assertThat(echoResponse.getResponse().getOutputSpeech(), nullValue());
		assertThat(echoResponse.getResponse().getShouldEndSession(), equalTo(Boolean.TRUE));
	}

	@Test
	public void buildsCorrectResponseWithSpeech() throws Exception {
		final EchoResponse echoResponse = EchoResponse.responseWithSpeech("Hi there");
		assertThat(echoResponse.getVersion(), equalTo("1.0"));
		assertThat(echoResponse.getResponse().getOutputSpeech().getText(), equalTo("Hi there"));
		assertThat(echoResponse.getResponse().getOutputSpeech().getType(), equalTo("PlainText"));
		assertThat(echoResponse.getResponse().getShouldEndSession(), equalTo(Boolean.TRUE));
	}
}
