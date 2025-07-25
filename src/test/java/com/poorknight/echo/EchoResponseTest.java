package com.poorknight.echo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

public class EchoResponseTest {

	@Test
	public void buildsCorrectNoOutputSpeechResponse() {
		final EchoResponse echoResponse = EchoResponse.noOutputSpeechResponse();
		assertThat(echoResponse.getVersion()).isEqualTo("1.0");
		assertThat(echoResponse.getResponse().getOutputSpeech()).isNull();
		assertThat(echoResponse.getResponse().getShouldEndSession()).isEqualTo(Boolean.TRUE);
	}

	@Test
	public void buildsCorrectResponseWithSpeech() {
		final EchoResponse echoResponse = EchoResponse.responseWithSpeech("Hi there");
		assertThat(echoResponse.getVersion()).isEqualTo("1.0");
		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("Hi there");
		assertThat(echoResponse.getResponse().getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(echoResponse.getResponse().getShouldEndSession()).isEqualTo(Boolean.TRUE);
	}

	@Test
	public void doesNotIncludeNullNodesWhenJsonSerializing() throws JsonProcessingException {
		final EchoResponse echoResponse = EchoResponse.noOutputSpeechResponse();

		String jsonString = new ObjectMapper().writeValueAsString(echoResponse);

		assertThat(jsonString).isEqualTo("{\"version\":\"1.0\",\"response\":{\"shouldEndSession\":true}}");
	}
}
