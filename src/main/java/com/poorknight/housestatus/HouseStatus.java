package com.poorknight.housestatus;

import org.joda.time.DateTime;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HouseStatus that = (HouseStatus) o;
		return Double.compare(that.houseTemp, houseTemp) == 0 &&
				Objects.equals(utcTime, that.utcTime) &&
				Objects.equals(localTime, that.localTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(utcTime, localTime, houseTemp);
	}
}
