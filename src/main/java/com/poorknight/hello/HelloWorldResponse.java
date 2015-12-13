package com.poorknight.hello;

public class HelloWorldResponse {

	private String version = "1.0";
	private HelloWorldResponseData response = new HelloWorldResponseData();
	private Boolean shouldEndSession = true;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public HelloWorldResponseData getResponse() {
		return response;
	}

	public void setResponse(HelloWorldResponseData response) {
		this.response = response;
	}

	public Boolean getShouldEndSession() {
		return shouldEndSession;
	}

	public void setShouldEndSession(Boolean shouldEndSession) {
		this.shouldEndSession = shouldEndSession;
	}
}
