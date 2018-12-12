package com.poorknight.housestatus.reports;


import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HouseStatusReport {

	private List<String> localTimes;

	private List<Double> houseTemperatures;

	private List<Double> thermostatSettings;

	public HouseStatusReport() {
	}

	public HouseStatusReport(List<String> localTimes, List<Double> houseTemperatures, List<Double> thermostatSettings) {
		this.localTimes = localTimes;
		this.houseTemperatures = houseTemperatures;
		this.thermostatSettings = thermostatSettings;
	}

	public List<String> getLocalTimes() {
		return Collections.unmodifiableList(localTimes);
	}

	public List<Double> getHouseTemperatures() {
		return Collections.unmodifiableList(houseTemperatures);
	}

	public List<Double> getThermostatSettings() {
		return Collections.unmodifiableList(thermostatSettings);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HouseStatusReport that = (HouseStatusReport) o;
		return Objects.equals(localTimes, that.localTimes) &&
				Objects.equals(houseTemperatures, that.houseTemperatures) &&
				Objects.equals(thermostatSettings, that.thermostatSettings);
	}

	@Override
	public int hashCode() {
		return Objects.hash(localTimes, houseTemperatures, thermostatSettings);
	}

	@Override
	public String toString() {
		return "HouseStatusReport{" +
				"localTimes=" + localTimes +
				", houseTemperatures=" + houseTemperatures +
				", thermostatSettings=" + thermostatSettings +
				'}';
	}
}
