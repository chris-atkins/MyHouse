package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.time.TimeFinder;
import org.joda.time.LocalDate;

import java.util.LinkedList;
import java.util.List;

public class HouseStatusReporter {

	private HouseStatusRepository repository;

	public HouseStatusReporter(HouseStatusRepository repository) {
		this.repository = repository;
	}

	public HouseStatusReport reportForDay(LocalDate date) {
		TimeFinder.UtcTimeRange rangeForLocalDay = new TimeFinder().getUtcRangeForLocalDay(date);
		List<HouseDataPoint> houseDataPoints = repository.retrieveHouseStatusFrom(rangeForLocalDay.getStartTime(), rangeForLocalDay.getEndTime());

		List<String> localTimes = new LinkedList<>();
		List<Double> houseTemperatures = new LinkedList<>();
		List<Double> thermostatSettings = new LinkedList<>();
		double lastTemp = Double.MIN_VALUE;
		double lastSetting = Double.MIN_VALUE;
		for (HouseDataPoint dataPoint : houseDataPoints) {

			double houseTemp = dataPoint.getThermostatStatus().getHouseTemp();
			double tempSetting = dataPoint.getThermostatStatus().getTempSetting();

			if (houseTemp == lastTemp && tempSetting == lastSetting) {
				continue;
			}

			localTimes.add(dataPoint.getLocalTime().toString("MM-dd hh:mm a"));
			houseTemperatures.add(houseTemp);
			thermostatSettings.add(tempSetting);

			lastTemp = houseTemp;
			lastSetting = tempSetting;
		}

		return new HouseStatusReport(localTimes, houseTemperatures, thermostatSettings);
	}
}
