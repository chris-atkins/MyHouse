package com.poorknight.echo.hello;

public class HelloWorldResponseData {

	private HelloWorldOutputSpeech outputSpeech = new HelloWorldOutputSpeech();
	private Boolean shouldEndSession = true;

	public HelloWorldOutputSpeech getOutputSpeech() {
		return outputSpeech;
	}

	public void setOutputSpeech(HelloWorldOutputSpeech outputSpeech) {
		this.outputSpeech = outputSpeech;
	}

	public Boolean getShouldEndSession() {
		return shouldEndSession;
	}

	public void setShouldEndSession(Boolean shouldEndSession) {
		this.shouldEndSession = shouldEndSession;
	}
}
