package com.poorknight.housestatus;

import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.housestatus.weather.WeatherRetriever;
import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.house.thermostat.ThermostatMessager;
import com.poorknight.house.thermostat.ThermostatStatus;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.poorknight.house.thermostat.ThermostatStatus.FurnaceState.HEAT_ON;
import static com.poorknight.house.thermostat.ThermostatStatus.ThermostatMode.FURNACE_MODE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HouseStatusRecorderTest {

	@InjectMocks
	private HouseStatusRecorder houseStatusRecorder;

	@Mock
	private HouseStatusRepository repository;

	@Mock
	private ThermostatMessager thermostatMessager;

	@Mock
	private TimeFinder currentTimeFinder;

	@Mock
	private WeatherRetriever weatherRetriever;

	@Test
	public void recordsHouseStatus() {
		ThermostatStatus thermostatStatus = new ThermostatStatus(55.53, 26.75, HEAT_ON, FURNACE_MODE);
		when(thermostatMessager.requestThermostatStatus()).thenReturn(thermostatStatus);

		DateTime utcTime = DateTime.parse("2018-03-04T22:55:53");
		DateTime localTime = DateTime.parse("2019-01-02T09:30");
		when(currentTimeFinder.getCurrentLocalTime()).thenReturn(localTime);
		when(currentTimeFinder.getUtcTimeFromLocalTime(localTime)).thenReturn(utcTime);

		WeatherStatus weatherStatus = new WeatherStatus(23.2, 4.23, 1.34, 234.5);
		when(weatherRetriever.findCurrentWeather()).thenReturn(weatherStatus);

		houseStatusRecorder.recordCurrentHouseStatus();

		verify(repository).addHouseStatus(utcTime, localTime, thermostatStatus, weatherStatus);
	}
}