package com.poorknight.housestatus;

import com.poorknight.thermostat.ThermostatMessager;
import com.poorknight.thermostat.ThermostatStatus;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;

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
		ThermostatStatus status = thermostatMessager.requestThermostatStatus();

		HouseStatus houseStatus = new HouseStatus(currentUtcTime, currentLocalTime, status.getCurrentTemp(), status.getTempSetting(), status.getFurnaceState().toString());
		repository.addHouseStatus(houseStatus);
	}
}
