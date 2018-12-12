package com.poorknight.endpoints.notification;

public class NotifyResponse {

	private String message;

	public NotifyResponse() {
		super();
	}

	public NotifyResponse(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
