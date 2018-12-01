package com.poorknight.housestatus;

import com.poorknight.thermostat.ThermostatMessager;
import com.poorknight.thermostat.ThermostatStatus;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HouseStatusRecorderTest {

	@InjectMocks
	private HouseStatusRecorder houseStatusRecorder;

	@Mock
	private HouseStatusRepository repository;

	@Mock
	private ThermostatMessager thermostatMessager;

	@Mock
	private TimeFinder currentTimeFinder;

	@Test
	public void recordsHouseStatus() {
		ThermostatStatus thermostatStatus = new ThermostatStatus(55.53, 26.75, ThermostatStatus.FurnaceState.HEAT_ON);
		when(thermostatMessager.requestThermostatStatus()).thenReturn(thermostatStatus);

		DateTime utcTime = DateTime.parse("2018-03-04T22:55:53");
		DateTime localTime = DateTime.parse("2019-01-02T09:30");
		when(currentTimeFinder.getCurrentLocalTime()).thenReturn(localTime);
		when(currentTimeFinder.getUtcTimeFromLocalTime(localTime)).thenReturn(utcTime);
		HouseStatus expectedHouseStatus = new HouseStatus(utcTime, localTime, 55.53, 26.75, "HEAT_ON");

		houseStatusRecorder.recordCurrentHouseStatus();

		verify(repository).addHouseStatus(expectedHouseStatus);
	}
}