package com.poorknight.housestatus.reports;

import java.util.Objects;

public class HouseDailySummary  {

	private Integer numberOfMinutesHeaterIsOn;

	private Double averageHouseTemperature;
	private Double averageExternalTemperature;
	private Double averageInternalExternalTemperatureDifference;
	private Double averageWindSpeed;

	private Integer averageTimeBetweenHeaterCyclesAtOneTemp;

	public HouseDailySummary() {

	}

	public HouseDailySummary(Integer numberOfMinutesHeaterIsOn, Double averageHouseTemperature, Double averageExternalTemperature, Double averageInternalExternalTemperatureDifference, Double averageWindSpeed, Integer averageTimeBetweenHeaterCyclesAtOneTemp) {
		this.numberOfMinutesHeaterIsOn = numberOfMinutesHeaterIsOn;
		this.averageHouseTemperature = averageHouseTemperature;
		this.averageExternalTemperature = averageExternalTemperature;
		this.averageInternalExternalTemperatureDifference = averageInternalExternalTemperatureDifference;
		this.averageWindSpeed = averageWindSpeed;
		this.averageTimeBetweenHeaterCyclesAtOneTemp = averageTimeBetweenHeaterCyclesAtOneTemp;
	}

	public Integer getNumberOfMinutesHeaterIsOn() {
		return numberOfMinutesHeaterIsOn;
	}

	public Double getAverageHouseTemperature() {
		return averageHouseTemperature;
	}

	public Double getAverageExternalTemperature() {
		return averageExternalTemperature;
	}

	public Double getAverageInternalExternalTemperatureDifference() {
		return averageInternalExternalTemperatureDifference;
	}

	public Double getAverageWindSpeed() {
		return averageWindSpeed;
	}

	public Integer getAverageTimeBetweenHeaterCyclesAtOneTemp() {
		return averageTimeBetweenHeaterCyclesAtOneTemp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HouseDailySummary that = (HouseDailySummary) o;
		return Objects.equals(numberOfMinutesHeaterIsOn, that.numberOfMinutesHeaterIsOn) &&
				Objects.equals(averageHouseTemperature, that.averageHouseTemperature) &&
				Objects.equals(averageExternalTemperature, that.averageExternalTemperature) &&
				Objects.equals(averageInternalExternalTemperatureDifference, that.averageInternalExternalTemperatureDifference) &&
				Objects.equals(averageWindSpeed, that.averageWindSpeed) &&
				Objects.equals(averageTimeBetweenHeaterCyclesAtOneTemp, that.averageTimeBetweenHeaterCyclesAtOneTemp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberOfMinutesHeaterIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageWindSpeed, averageTimeBetweenHeaterCyclesAtOneTemp);
	}

	@Override
	public String toString() {
		return "HouseDailySummary{" +
				"numberOfMinutesHeaterIsOn=" + numberOfMinutesHeaterIsOn +
				", averageHouseTemperature=" + averageHouseTemperature +
				", averageExternalTemperature=" + averageExternalTemperature +
				", averageInternalExternalTemperatureDifference=" + averageInternalExternalTemperatureDifference +
				", averageWindSpeed=" + averageWindSpeed +
				", averageTimeBetweenHeaterCyclesAtOneTemp=" + averageTimeBetweenHeaterCyclesAtOneTemp +
				'}';
	}
}
