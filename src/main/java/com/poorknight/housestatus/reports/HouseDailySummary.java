package com.poorknight.housestatus.reports;

import java.util.Objects;

public class HouseDailySummary  {

	private Integer numberOfMinutesHeaterIsOn;
	private Integer numberOfMinutesACIsOn;

	private Double averageHouseTemperature;
	private Double averageExternalTemperature;
	private Double averageInternalExternalTemperatureDifference;
	private Double averageHouseTempSetting;

	private Double averageWindSpeed;

	private Double averageTimeBetweenHeaterCyclesAtOneTemp;
	private Double averageTimeBetweenACCyclesAtOneTemp;

	private Integer numberOfMinutesDataExistsFor;

	public HouseDailySummary() {

	}

	public HouseDailySummary(Integer numberOfMinutesDataExistsFor, Integer numberOfMinutesHeaterIsOn, Integer numberOfMinutesACIsOn, Double averageHouseTemperature, Double averageExternalTemperature, Double averageInternalExternalTemperatureDifference, Double averageHouseTempSetting, Double averageWindSpeed, Double averageTimeBetweenHeaterCyclesAtOneTemp, Double averageTimeBetweenACCyclesAtOneTemp) {
		this.numberOfMinutesDataExistsFor = numberOfMinutesDataExistsFor;
		this.numberOfMinutesHeaterIsOn = numberOfMinutesHeaterIsOn;
		this.numberOfMinutesACIsOn = numberOfMinutesACIsOn;
		this.averageHouseTemperature = averageHouseTemperature;
		this.averageExternalTemperature = averageExternalTemperature;
		this.averageInternalExternalTemperatureDifference = averageInternalExternalTemperatureDifference;
		this.averageHouseTempSetting = averageHouseTempSetting;
		this.averageWindSpeed = averageWindSpeed;
		this.averageTimeBetweenHeaterCyclesAtOneTemp = averageTimeBetweenHeaterCyclesAtOneTemp;
		this.averageTimeBetweenACCyclesAtOneTemp = averageTimeBetweenACCyclesAtOneTemp;
	}

	public Integer getNumberOfMinutesHeaterIsOn() {
		return numberOfMinutesHeaterIsOn;
	}

	public Integer getNumberOfMinutesACIsOn() {
		return numberOfMinutesACIsOn;
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

	public Double getAverageHouseTempSetting() {
		return averageHouseTempSetting;
	}

	public Double getAverageWindSpeed() {
		return averageWindSpeed;
	}

	public Double getAverageTimeBetweenHeaterCyclesAtOneTemp() {
		return averageTimeBetweenHeaterCyclesAtOneTemp;
	}

	public Double getAverageTimeBetweenACCyclesAtOneTemp() {
		return averageTimeBetweenACCyclesAtOneTemp;
	}

	public Integer getNumberOfMinutesDataExistsFor() {
		return numberOfMinutesDataExistsFor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HouseDailySummary that = (HouseDailySummary) o;
		return Objects.equals(numberOfMinutesHeaterIsOn, that.numberOfMinutesHeaterIsOn) &&
				Objects.equals(numberOfMinutesACIsOn, that.numberOfMinutesACIsOn) &&
				Objects.equals(averageHouseTemperature, that.averageHouseTemperature) &&
				Objects.equals(averageExternalTemperature, that.averageExternalTemperature) &&
				Objects.equals(averageInternalExternalTemperatureDifference, that.averageInternalExternalTemperatureDifference) &&
				Objects.equals(averageHouseTempSetting, that.averageHouseTempSetting) &&
				Objects.equals(averageWindSpeed, that.averageWindSpeed) &&
				Objects.equals(averageTimeBetweenHeaterCyclesAtOneTemp, that.averageTimeBetweenHeaterCyclesAtOneTemp) &&
				Objects.equals(averageTimeBetweenACCyclesAtOneTemp, that.averageTimeBetweenACCyclesAtOneTemp) &&
				Objects.equals(numberOfMinutesDataExistsFor, that.numberOfMinutesDataExistsFor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberOfMinutesHeaterIsOn, numberOfMinutesACIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageHouseTempSetting, averageWindSpeed, averageTimeBetweenHeaterCyclesAtOneTemp, averageTimeBetweenACCyclesAtOneTemp, numberOfMinutesDataExistsFor);
	}

	@Override
	public String toString() {
		return "HouseDailySummary{" +
				"numberOfMinutesHeaterIsOn=" + numberOfMinutesHeaterIsOn +
				", numberOfMinutesACIsOn=" + numberOfMinutesACIsOn +
				", averageHouseTemperature=" + averageHouseTemperature +
				", averageExternalTemperature=" + averageExternalTemperature +
				", averageInternalExternalTemperatureDifference=" + averageInternalExternalTemperatureDifference +
				", averageHouseTempSetting=" + averageHouseTempSetting +
				", averageWindSpeed=" + averageWindSpeed +
				", averageTimeBetweenHeaterCyclesAtOneTemp=" + averageTimeBetweenHeaterCyclesAtOneTemp +
				", averageTimeBetweenACCyclesAtOneTemp=" + averageTimeBetweenACCyclesAtOneTemp +
				", numberOfMinutesDataExistsFor=" + numberOfMinutesDataExistsFor +
				'}';
	}
}
