package com.poorknight.housestatus.reports;

import java.util.Objects;

public class HouseDailySummary  {

	private Integer numberOfMinutesHeaterIsOn;

	private Double averageHouseTemperature;
	private Double averageExternalTemperature;
	private Double averageInternalExternalTemperatureDifference;
	private Double averageHouseTempSetting;

	private Double averageWindSpeed;

	private Double averageTimeBetweenHeaterCyclesAtOneTemp;

	private Integer numberOfMinutesDataExistsFor;

	public HouseDailySummary() {

	}

	public HouseDailySummary(Integer numberOfMinutesDataExistsFor, Integer numberOfMinutesHeaterIsOn, Double averageHouseTemperature, Double averageExternalTemperature, Double averageInternalExternalTemperatureDifference, Double averageHouseTempSetting, Double averageWindSpeed, Double averageTimeBetweenHeaterCyclesAtOneTemp) {
		this.numberOfMinutesDataExistsFor = numberOfMinutesDataExistsFor;
		this.numberOfMinutesHeaterIsOn = numberOfMinutesHeaterIsOn;
		this.averageHouseTemperature = averageHouseTemperature;
		this.averageExternalTemperature = averageExternalTemperature;
		this.averageInternalExternalTemperatureDifference = averageInternalExternalTemperatureDifference;
		this.averageHouseTempSetting = averageHouseTempSetting;
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

	public Double getAverageTimeBetweenHeaterCyclesAtOneTemp() {
		return averageTimeBetweenHeaterCyclesAtOneTemp;
	}

	public Integer getNumberOfMinutesDataExistsFor() {
		return numberOfMinutesDataExistsFor;
	}

	public Double getAverageHouseTempSetting() {
		return averageHouseTempSetting;
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
				Objects.equals(averageHouseTempSetting, that.averageHouseTempSetting) &&
				Objects.equals(averageWindSpeed, that.averageWindSpeed) &&
				Objects.equals(averageTimeBetweenHeaterCyclesAtOneTemp, that.averageTimeBetweenHeaterCyclesAtOneTemp) &&
				Objects.equals(numberOfMinutesDataExistsFor, that.numberOfMinutesDataExistsFor);
	}

	@Override
	public int hashCode() {

		return Objects.hash(numberOfMinutesHeaterIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageHouseTempSetting, averageWindSpeed, averageTimeBetweenHeaterCyclesAtOneTemp, numberOfMinutesDataExistsFor);
	}

	@Override
	public String toString() {
		return "HouseDailySummary{" +
				"numberOfMinutesHeaterIsOn=" + numberOfMinutesHeaterIsOn +
				", averageHouseTemperature=" + averageHouseTemperature +
				", averageExternalTemperature=" + averageExternalTemperature +
				", averageInternalExternalTemperatureDifference=" + averageInternalExternalTemperatureDifference +
				", averageHouseTempSetting=" + averageHouseTempSetting +
				", averageWindSpeed=" + averageWindSpeed +
				", averageTimeBetweenHeaterCyclesAtOneTemp=" + averageTimeBetweenHeaterCyclesAtOneTemp +
				", numberOfMinutesDataExistsFor=" + numberOfMinutesDataExistsFor +
				'}';
	}
}
