package com.poorknight.endpoints.houseip;

public class HouseIpResponse {

	private String message;

	public HouseIpResponse() {
		//empty constructor for json marshalling
	}

	public HouseIpResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
