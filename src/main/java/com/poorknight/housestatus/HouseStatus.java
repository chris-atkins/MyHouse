package com.poorknight.housestatus;

import org.joda.time.DateTime;

public class HouseStatus {

	private final DateTime utcTime;
	private final DateTime localTime;
	private final double houseTemp;

	public HouseStatus(DateTime utcTime, DateTime localTime, double houseTemp) {
		this.utcTime = utcTime;
		this.localTime = localTime;
		this.houseTemp = houseTemp;
	}

	public DateTime getUtcTime() {
		return utcTime;
	}

	public DateTime getLocalTime() {
		return localTime;
	}

	public double getHouseTemp() {
		return houseTemp;
	}
}
