package com.poorknight.housestatus;

import com.poorknight.thermostat.ThermostatMessager;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class HouseStatusRecorder {

	private final TimeFinder timeFinder;
	private final ThermostatMessager thermostatMessager;
	private final HouseStatusRepository repository;

	public HouseStatusRecorder(TimeFinder timeFinder, ThermostatMessager thermostatMessager, HouseStatusRepository repository) {
		this.timeFinder = timeFinder;
		this.thermostatMessager = thermostatMessager;
		this.repository = repository;
	}

	public void recordCurrentHouseStatus() {
		DateTime currentLocalTime = timeFinder.getCurrentLocalTime();
		DateTime currentUtcTime = timeFinder.getUtcTimeFromLocalTime(currentLocalTime);
		BigDecimal currentTemp = thermostatMessager.requestCurrentTemp();

		HouseStatus houseStatus = new HouseStatus(currentUtcTime, currentLocalTime, currentTemp.doubleValue());
		repository.addHouseStatus(houseStatus);
	}
}
