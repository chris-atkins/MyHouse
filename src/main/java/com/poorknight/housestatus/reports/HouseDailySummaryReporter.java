package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.house.thermostat.ThermostatStatus.FurnaceState;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class HouseDailySummaryReporter {

	private HouseStatusRepository houseStatusRepository;

	public HouseDailySummaryReporter(HouseStatusRepository houseStatusRepository) {
		this.houseStatusRepository = houseStatusRepository;
	}

	public HouseDailySummary summaryForDay(LocalDate date) {
		TimeFinder.UtcTimeRange utcTimeRange = new TimeFinder().getUtcRangeForLocalDay(date);

		List<HouseDataPoint> dataPoints = houseStatusRepository.retrieveHouseStatusFrom(utcTimeRange.getStartTime(), utcTimeRange.getEndTime());
		if (dataPoints.size() == 0) {
			return new HouseDailySummary(0, null, null, null, null, null, null, null, null, null);
		}


		Integer numberOfMinutesDataExistsFor = dataPoints.size();
		Integer numberOfMinutesHeaterIsOn = findNumberOfMinutesHeaterIsOn(dataPoints);
		Integer numberOfMinutesACIsOn = findNumberOfMinutesACIsOn(dataPoints);
		Double averageHouseTemperature = findAverageHouseTemp(dataPoints);
		Double averageExternalTemperature = findAverageExternalTemp(dataPoints);
		Double averageInternalExternalTemperatureDifference = averageExternalTemperature - averageHouseTemperature;
		Double averageHouseTempSetting = findAverageHouseTempSetting(dataPoints);
		Double averageWindSpeed = findAverageWindSpeed(dataPoints);
		Double averageTimeBetweenHeaterCyclesAtOneTemp = findAverageTimeBetweenHeaterCyclesAtOneTemp(dataPoints);
		Double averageTimeBetweenACCyclesAtOneTemp = findAverageTimeBetweenACCyclesAtOneTemp(dataPoints);

		HouseDailySummary summary = new HouseDailySummary(numberOfMinutesDataExistsFor, numberOfMinutesHeaterIsOn, numberOfMinutesACIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageHouseTempSetting, averageWindSpeed, averageTimeBetweenHeaterCyclesAtOneTemp, averageTimeBetweenACCyclesAtOneTemp);
		return summary;
	}

	private Integer findNumberOfMinutesHeaterIsOn(List<HouseDataPoint> dataPoints) {
		return countFurnaceStateInstances(dataPoints, FurnaceState.HEAT_ON);
	}

	private Integer findNumberOfMinutesACIsOn(List<HouseDataPoint> dataPoints) {
		return countFurnaceStateInstances(dataPoints, FurnaceState.AC_ON);
	}

	private Integer countFurnaceStateInstances(List<HouseDataPoint> dataPoints, FurnaceState furnaceState) {
		int count = 0;
		for (HouseDataPoint dataPoint : dataPoints) {
			if (dataPoint.getThermostatStatus().getFurnaceState() == furnaceState) {
				count++;
			}
		}
		return count;
	}

	private Double findAverageHouseTemp(List<HouseDataPoint> dataPoints) {
		double sum = 0;
		for (HouseDataPoint dataPoint : dataPoints) {
			sum += dataPoint.getThermostatStatus().getHouseTemp();
		}
		return sum / dataPoints.size();
	}

	private Double findAverageExternalTemp(List<HouseDataPoint> dataPoints) {
		double sum = 0;
		for (HouseDataPoint dataPoint : dataPoints) {
			sum += dataPoint.getWeatherStatus().getTempFahrenheit();
		}
		return sum / dataPoints.size();
	}

	private Double findAverageHouseTempSetting(List<HouseDataPoint> dataPoints) {
		double sum = 0;
		for (HouseDataPoint dataPoint : dataPoints) {
			sum += dataPoint.getThermostatStatus().getTempSetting();
		}
		return sum / dataPoints.size();
	}

	private Double findAverageWindSpeed(List<HouseDataPoint> dataPoints) {
		double sum = 0;
		for (HouseDataPoint dataPoint : dataPoints) {
			sum += dataPoint.getWeatherStatus().getWindSpeedMph();
		}
		return sum / dataPoints.size();
	}

	private Double findAverageTimeBetweenHeaterCyclesAtOneTemp(List<HouseDataPoint> dataPoints) {
		return findAverageTimeBetweenCycles(dataPoints, FurnaceState.HEAT_ON);
	}

	private Double findAverageTimeBetweenACCyclesAtOneTemp(List<HouseDataPoint> dataPoints) {
		return findAverageTimeBetweenCycles(dataPoints, FurnaceState.AC_ON);
	}

	private Double findAverageTimeBetweenCycles(List<HouseDataPoint> dataPoints, FurnaceState targetState) {
		if (dataPoints.size() == 0) {
			return null;
		}

		Double numberOfCycles = 0d;
		Double totalTime = 0d;

		FurnaceState lastFurnaceState = null;
		Double lastTempSetting = dataPoints.get(0).getThermostatStatus().getTempSetting();
		DateTime lastUtcTime = dataPoints.get(0).getUtcTime();
		boolean firstCycleStarted = false;
		Double currentCycleTime = 0d;
		boolean badCycle = false;

		for (HouseDataPoint dataPoint : dataPoints) {
			FurnaceState furnaceState = dataPoint.getThermostatStatus().getFurnaceState();
			Double tempSetting = dataPoint.getThermostatStatus().getTempSetting();
			DateTime currentUtcTime = dataPoint.getUtcTime();

			if (!lastTempSetting.equals(tempSetting) && (furnaceState == FurnaceState.OFF || lastFurnaceState == FurnaceState.OFF)) {
				badCycle = true;
			}

			if (furnaceState != FurnaceState.OFF && furnaceState != targetState) {
				badCycle = true;
			}

			if (lastUtcTime.plusSeconds(90).isBefore(currentUtcTime)) {
				badCycle = true;
			}

			if (furnaceState == targetState && lastFurnaceState != targetState && firstCycleStarted) {
				if (!badCycle) {
					numberOfCycles++;
					totalTime += currentCycleTime;
				}
				currentCycleTime = 0d;
				badCycle = false;
			}

			if (furnaceState == FurnaceState.OFF && firstCycleStarted) {
				currentCycleTime++;
			}

			if (furnaceState == targetState) {
				firstCycleStarted = true;
			}

			lastTempSetting = tempSetting;
			lastFurnaceState = furnaceState;
			lastUtcTime = currentUtcTime;
		}


		return totalTime / numberOfCycles;
	}
}
