package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class HouseDailySummaryReporter {

	HouseStatusRepository houseStatusRepository;

	public HouseDailySummaryReporter(HouseStatusRepository houseStatusRepository) {
		this.houseStatusRepository = houseStatusRepository;
	}

	public HouseDailySummary summaryForDay(LocalDate date) {
		List<HouseDataPoint> dataPoints = houseStatusRepository.retrieveHouseStatusFrom(DateTime.now(), DateTime.now());

		Integer numberOfMinutesHeaterIsOn = 0;
		Double averageHouseTemperature = findAverageHouseTemperature(dataPoints);
		Double averageExternalTemperature = 0d;
		Double averageInternalExternalTemperatureDifference = 0d;
		Double averageWindSpeed = 0d;
		Integer averageTimeBetweenHeaterCyclesAtOneTemp = 0;
		HouseDailySummary summary = new HouseDailySummary(numberOfMinutesHeaterIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageWindSpeed, averageTimeBetweenHeaterCyclesAtOneTemp);
		return summary;
	}

	private Double findAverageHouseTemperature(List<HouseDataPoint> dataPoints) {
		double sum = 0;
		for (HouseDataPoint dataPoint : dataPoints) {
			sum += dataPoint.getInternalTemp();
		}
		return sum / dataPoints.size();
	}
}
