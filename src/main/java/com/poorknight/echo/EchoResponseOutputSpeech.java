package com.poorknight.echo;

public class EchoResponseOutputSpeech {

	private String type = "PlainText";
	private String text = "Hi there.";

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}
}
