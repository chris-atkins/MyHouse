package com.poorknight.echo;

public class EchoResponseData {

	private EchoResponseOutputSpeech outputSpeech = new EchoResponseOutputSpeech();
	private Boolean shouldEndSession = true;
	
	public EchoResponseOutputSpeech getOutputSpeech() {
		return outputSpeech;
	}

	public void setOutputSpeech(final EchoResponseOutputSpeech outputSpeech) {
		this.outputSpeech = outputSpeech;
	}

	public Boolean getShouldEndSession() {
		return shouldEndSession;
	}

	public void setShouldEndSession(final Boolean shouldEndSession) {
		this.shouldEndSession = shouldEndSession;
	}
}
