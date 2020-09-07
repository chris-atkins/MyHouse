package com.poorknight.housestatus;

import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.housestatus.weather.WeatherRetriever;
import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.house.thermostat.ThermostatMessager;
import com.poorknight.house.thermostat.ThermostatStatus;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;

public class HouseStatusRecorder {

	private final TimeFinder timeFinder;
	private final ThermostatMessager thermostatMessager;
	private final WeatherRetriever weatherRetriever;
	private final HouseStatusRepository repository;

	public HouseStatusRecorder(TimeFinder timeFinder, ThermostatMessager thermostatMessager, WeatherRetriever weatherRetriever, HouseStatusRepository repository) {
		this.timeFinder = timeFinder;
		this.thermostatMessager = thermostatMessager;
		this.weatherRetriever = weatherRetriever;
		this.repository = repository;
	}

	public void recordCurrentHouseStatus() {
		DateTime currentLocalTime = timeFinder.getCurrentLocalTime();
		DateTime currentUtcTime = timeFinder.getUtcTimeFromLocalTime(currentLocalTime);
		WeatherStatus weatherStatus = weatherRetriever.findCurrentWeather();
		ThermostatStatus thermostatStatus = thermostatMessager.requestThermostatStatus();

		repository.addHouseStatus(currentUtcTime, currentLocalTime, thermostatStatus, weatherStatus);
	}
}
