package com.poorknight.echo;

public class EchoResponse {

	private String version = "1.0";
	private EchoResponseData response = new EchoResponseData();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public EchoResponseData getResponse() {
		return response;
	}

	public void setResponse(EchoResponseData response) {
		this.response = response;
	}
}
