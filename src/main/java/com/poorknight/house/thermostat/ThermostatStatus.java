package com.poorknight.house.thermostat;

import java.util.Objects;

public class ThermostatStatus {

	private double houseTemp;
	private double tempSetting;
	private FurnaceState furnaceState;
	private ThermostatMode thermostatMode;

	public ThermostatStatus(double houseTemp, double tempSetting, FurnaceState furnaceState, ThermostatMode thermostatMode) {
		this.houseTemp = houseTemp;
		this.tempSetting = tempSetting;
		this.furnaceState = furnaceState;
		this.thermostatMode = thermostatMode;
	}

	public double getHouseTemp() {
		return houseTemp;
	}

	public double getTempSetting() {
		return tempSetting;
	}

	public FurnaceState getFurnaceState() {
		return furnaceState;
	}

	public ThermostatMode getThermostatMode() {
		return this.thermostatMode;
	}

	public enum FurnaceState {
		HEAT_ON, AC_ON, OFF
	}

	public enum ThermostatMode {
		OFF, FURNACE_MODE, AC_MODE, AUTO_MODE
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ThermostatStatus that = (ThermostatStatus) o;
		return Double.compare(that.houseTemp, houseTemp) == 0 &&
				Double.compare(that.tempSetting, tempSetting) == 0 &&
				furnaceState == that.furnaceState &&
				thermostatMode == that.thermostatMode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(houseTemp, tempSetting, furnaceState, thermostatMode);
	}

	@Override
	public String toString() {
		return "ThermostatStatus{" +
				"houseTemp=" + houseTemp +
				", tempSetting=" + tempSetting +
				", furnaceState=" + furnaceState +
				", thermostatMode=" + thermostatMode +
				'}';
	}
}
