package com.poorknight.rest.houseip;

public class HouseIpRequest {

	private String houseIp;

	public HouseIpRequest() {
		//empty constructor for json marshalling
	}

	public HouseIpRequest(String houseIp) {
		this.houseIp = houseIp;
	}

	public String getHouseIp() {
		return houseIp;
	}

	public void setHouseIp(String houseIp) {
		this.houseIp = houseIp;
	}
}
