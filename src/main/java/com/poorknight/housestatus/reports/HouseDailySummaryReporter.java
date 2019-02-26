package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.thermostat.ThermostatStatus.FurnaceState;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

public class HouseDailySummaryReporter {

	HouseStatusRepository houseStatusRepository;

	public HouseDailySummaryReporter(HouseStatusRepository houseStatusRepository) {
		this.houseStatusRepository = houseStatusRepository;
	}

	public HouseDailySummary summaryForDay(LocalDate date) {
		DateTime houseLocalMidnight = date.toDateTime(LocalTime.MIDNIGHT, DateTimeZone.forID("America/Detroit"));
		DateTime startTimeUtc = houseLocalMidnight.toDateTime(DateTimeZone.UTC);
		DateTime endTimeUtc = startTimeUtc.plusDays(1).minusMillis(1);

		List<HouseDataPoint> dataPoints = houseStatusRepository.retrieveHouseStatusFrom(startTimeUtc, endTimeUtc);
		if (dataPoints.size() == 0) {
			return new HouseDailySummary(0, null, null, null, null, null, null, null);
		}


		Integer numberOfMinutesDataExistsFor = dataPoints.size();
		Integer numberOfMinutesHeaterIsOn = findNumberOfMinutesHeaterIsOn(dataPoints);
		Double averageHouseTemperature = findAverageHouseTemp(dataPoints);
		Double averageExternalTemperature = findAverageExternalTemp(dataPoints);
		Double averageInternalExternalTemperatureDifference = averageExternalTemperature - averageHouseTemperature;
		Double averageHouseTempSetting = findAverageHouseTempSetting(dataPoints);
		Double averageWindSpeed = findAverageWindSpeed(dataPoints);
		Double averageTimeBetweenHeaterCyclesAtOneTemp = findAverageTimeBetweenHeaterCyclesAtOneTemp(dataPoints);

		HouseDailySummary summary = new HouseDailySummary(numberOfMinutesDataExistsFor, numberOfMinutesHeaterIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageHouseTempSetting, averageWindSpeed, averageTimeBetweenHeaterCyclesAtOneTemp);
		return summary;
	}

	private Integer findNumberOfMinutesHeaterIsOn(List<HouseDataPoint> dataPoints) {
		int count = 0;
		for (HouseDataPoint dataPoint : dataPoints) {
			if (dataPoint.getThermostatStatus().getFurnaceState() == FurnaceState.HEAT_ON) {
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

			if (! lastTempSetting.equals(tempSetting) && (furnaceState == FurnaceState.OFF || lastFurnaceState == FurnaceState.OFF)) {
				badCycle = true;
			}

			if (lastUtcTime.plusSeconds(90).isBefore(currentUtcTime)) {
				badCycle = true;
			}

			if (furnaceState == FurnaceState.HEAT_ON && lastFurnaceState == FurnaceState.OFF && firstCycleStarted) {
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

			if (furnaceState == FurnaceState.HEAT_ON) {
				firstCycleStarted = true;
			}

			lastTempSetting = tempSetting;
			lastFurnaceState = furnaceState;
			lastUtcTime = currentUtcTime;
		}


		return totalTime / numberOfCycles;
	}
}
