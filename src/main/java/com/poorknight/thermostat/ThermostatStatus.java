package com.poorknight.thermostat;

import java.util.Objects;

public class ThermostatStatus {


	private double currentTemp;
	private double tempSetting;
	private FurnaceState furnaceState;

	public ThermostatStatus(double currentTemp, double tempSetting, FurnaceState furnaceState) {
		this.currentTemp = currentTemp;
		this.tempSetting = tempSetting;
		this.furnaceState = furnaceState;
	}

	public double getCurrentTemp() {
		return currentTemp;
	}

	public double getTempSetting() {
		return tempSetting;
	}

	public FurnaceState getFurnaceState() {
		return furnaceState;
	}

	public static enum FurnaceState {
		HEAT_ON, OFF
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ThermostatStatus that = (ThermostatStatus) o;
		return Double.compare(that.currentTemp, currentTemp) == 0 &&
				Double.compare(that.tempSetting, tempSetting) == 0 &&
				furnaceState == that.furnaceState;
	}

	@Override
	public int hashCode() {

		return Objects.hash(currentTemp, tempSetting, furnaceState);
	}

	@Override
	public String toString() {
		return "ThermostatStatus{" +
				"currentTemp=" + currentTemp +
				", tempSetting=" + tempSetting +
				", furnaceState=" + furnaceState +
				'}';
	}
}
