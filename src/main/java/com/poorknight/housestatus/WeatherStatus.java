package com.poorknight.housestatus;

import java.util.Objects;

public class WeatherStatus {

	private final Double tempFahrenheit;
	private final Double windSpeedMph;
	private final Double humidityPercent;
	private final Double pressureHPa;

	public WeatherStatus(Double tempFahrenheit, Double windSpeedMph, Double humidityPercent, Double pressureHPa) {
		this.tempFahrenheit = tempFahrenheit;
		this.windSpeedMph = windSpeedMph;
		this.humidityPercent = humidityPercent;
		this.pressureHPa = pressureHPa;
	}

	public Double getTempFahrenheit() {
		return tempFahrenheit;
	}

	public Double getWindSpeedMph() {
		return windSpeedMph;
	}

	public Double getHumidityPercent() {
		return humidityPercent;
	}

	public Double getPressureHPa() {
		return pressureHPa;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WeatherStatus that = (WeatherStatus) o;
		return Objects.equals(tempFahrenheit, that.tempFahrenheit) &&
				Objects.equals(windSpeedMph, that.windSpeedMph) &&
				Objects.equals(humidityPercent, that.humidityPercent) &&
				Objects.equals(pressureHPa, that.pressureHPa);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tempFahrenheit, windSpeedMph, humidityPercent, pressureHPa);
	}

	@Override
	public String toString() {
		return "WeatherStatus{" +
				"tempFahrenheit=" + tempFahrenheit +
				", windSpeedMph=" + windSpeedMph +
				", humidityPercent=" + humidityPercent +
				", pressureHPa=" + pressureHPa +
				'}';
	}
}
