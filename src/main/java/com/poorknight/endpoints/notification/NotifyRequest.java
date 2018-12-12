package com.poorknight.endpoints.notification;

public class NotifyRequest {

	private String messageContent;

	public NotifyRequest() {
		super();
	}

	public NotifyRequest(final String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(final String messageContent) {
		this.messageContent = messageContent;
	}
}
