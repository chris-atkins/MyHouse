package com.poorknight.echo;

public class EchoResponse {

	private String version = "1.0";
	private EchoResponseData response = new EchoResponseData();

	public static EchoResponse noOutputSpeechResponse() {
		final EchoResponse echoResponse = new EchoResponse();
		echoResponse.getResponse().setOutputSpeech(null);
		return echoResponse;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public EchoResponseData getResponse() {
		return response;
	}

	public void setResponse(final EchoResponseData response) {
		this.response = response;
	}
}
