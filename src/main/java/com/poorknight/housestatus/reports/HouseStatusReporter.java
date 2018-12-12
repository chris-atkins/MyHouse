package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

public class HouseStatusReporter {

	private HouseStatusRepository repository;

	public HouseStatusReporter(HouseStatusRepository repository) {
		this.repository = repository;
	}

	public HouseStatusReport reportForLast24Hours() {
		DateTime endTime = DateTime.now();
		DateTime startTime = endTime.minusDays(1);
		List<HouseDataPoint> houseDataPoints = repository.retrieveHouseStatusFrom(startTime, endTime);

		List<String> localTimes = new LinkedList<>();
		List<Double> houseTemperatures = new LinkedList<>();
		List<Double> thermostatSettings = new LinkedList<>();
		for (HouseDataPoint dataPoint : houseDataPoints) {
			localTimes.add(dataPoint.getLocalTime().toString("MM-dd hh:mm a"));
			houseTemperatures.add(dataPoint.getInternalTemp());
			thermostatSettings.add(dataPoint.getThermostatTempSetting());
		}

		return new HouseStatusReport(localTimes, houseTemperatures, thermostatSettings);
	}
}
