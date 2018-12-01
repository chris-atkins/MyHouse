package com.poorknight.housestatus;

import org.joda.time.DateTime;

import java.util.Objects;

public class HouseStatus {

	private final DateTime utcTime;
	private final DateTime localTime;
	private final double houseTemp;
	private final double tempSetting;
	private final String furnaceState;

	public HouseStatus(DateTime utcTime, DateTime localTime, double houseTemp, double tempSetting, String furnaceState) {
		this.utcTime = utcTime;
		this.localTime = localTime;
		this.houseTemp = houseTemp;
		this.tempSetting = tempSetting;
		this.furnaceState = furnaceState;
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

	public double getTempSetting() {
		return tempSetting;
	}

	public String getFurnaceState() {
		return furnaceState;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HouseStatus that = (HouseStatus) o;
		return Double.compare(that.houseTemp, houseTemp) == 0 &&
				Double.compare(that.tempSetting, tempSetting) == 0 &&
				Objects.equals(utcTime, that.utcTime) &&
				Objects.equals(localTime, that.localTime) &&
				Objects.equals(furnaceState, that.furnaceState);
	}

	@Override
	public int hashCode() {

		return Objects.hash(utcTime, localTime, houseTemp, tempSetting, furnaceState);
	}

	@Override
	public String toString() {
		return "HouseStatus{" +
				"utcTime=" + utcTime +
				", localTime=" + localTime +
				", houseTemp=" + houseTemp +
				", tempSetting=" + tempSetting +
				", furnaceState='" + furnaceState + '\'' +
				'}';
	}
}
