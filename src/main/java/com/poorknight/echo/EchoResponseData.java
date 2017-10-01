package com.poorknight.echo;

public class EchoResponseData {

	private EchoResponseOutputSpeech outputSpeech = new EchoResponseOutputSpeech();
	private Boolean shouldEndSession = true;
	private List<EchoDirective> directives = Arrays.asList(new EchoDirective());

	public List<EchoDirective> getDirectives() {
		return directives;
	}

	public void setDirectives(final List<EchoDirective> directives) {
		this.directives = directives;
	}

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
