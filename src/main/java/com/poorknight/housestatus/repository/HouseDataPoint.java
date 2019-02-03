package com.poorknight.housestatus.repository;

import org.joda.time.DateTime;

import java.util.Objects;

public class HouseDataPoint {

	private DateTime localTime;
	private Double internalTemp;
	private Double thermostatTempSetting;

	public HouseDataPoint(DateTime localTime, Double internalTemp, Double thermostatTempSetting) {
		this.localTime = localTime;
		this.internalTemp = internalTemp;
		this.thermostatTempSetting = thermostatTempSetting;
	}

	public DateTime getLocalTime() {
		return localTime;
	}

	public Double getInternalTemp() {
		return internalTemp;
	}

	public Double getThermostatTempSetting() {
		return thermostatTempSetting;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HouseDataPoint that = (HouseDataPoint) o;
		return Objects.equals(localTime, that.localTime) &&
				Objects.equals(internalTemp, that.internalTemp) &&
				Objects.equals(thermostatTempSetting, that.thermostatTempSetting);
	}

	@Override
	public int hashCode() {
		return Objects.hash(localTime, internalTemp, thermostatTempSetting);
	}

	@Override
	public String toString() {
		return "HouseDataPoint{" +
				"localTime=" + localTime +
				", internalTemp=" + internalTemp +
				", thermostatTempSetting=" + thermostatTempSetting +
				'}';
	}
}
