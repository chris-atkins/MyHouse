package com.poorknight.housestatus.repository;

import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.house.thermostat.ThermostatStatus;
import org.joda.time.DateTime;

import java.util.Objects;

public class HouseDataPoint {

	private DateTime localTime;
	private DateTime utcTime;

	private ThermostatStatus thermostatStatus;
	private WeatherStatus weatherStatus;

	public HouseDataPoint(DateTime localTime, DateTime utcTime, ThermostatStatus thermostatStatus, WeatherStatus weatherStatus) {
		this.localTime = localTime;
		this.utcTime = utcTime;
		this.thermostatStatus = thermostatStatus;
		this.weatherStatus = weatherStatus;
	}

	public DateTime getLocalTime() {
		return localTime;
	}

	public DateTime getUtcTime() {
		return utcTime;
	}

	public ThermostatStatus getThermostatStatus() {
		return thermostatStatus;
	}

	public WeatherStatus getWeatherStatus() {
		return weatherStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HouseDataPoint dataPoint = (HouseDataPoint) o;
		return Objects.equals(localTime, dataPoint.localTime) &&
				Objects.equals(utcTime, dataPoint.utcTime) &&
				Objects.equals(thermostatStatus, dataPoint.thermostatStatus) &&
				Objects.equals(weatherStatus, dataPoint.weatherStatus);
	}

	@Override
	public int hashCode() {

		return Objects.hash(localTime, utcTime, thermostatStatus, weatherStatus);
	}

	@Override
	public String toString() {
		return "HouseDataPoint{" +
				"localTime=" + localTime +
				", utcTime=" + utcTime +
				", thermostatStatus=" + thermostatStatus +
				", weatherStatus=" + weatherStatus +
				'}';
	}
}
