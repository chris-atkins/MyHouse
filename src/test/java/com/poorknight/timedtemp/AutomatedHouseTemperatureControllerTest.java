package com.poorknight.timedtemp;

import com.poorknight.thermostat.ThermostatMessager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AutomatedHouseTemperatureControllerTest {

	private AutomatedHouseTemperatureController controller;

	@Mock
	private CurrentLocalTimeFinder currentLocalTimeFinder;

	@Mock
	private ThermostatMessager thermostatMessager;

	@Before
	public void setUp() {
		controller = new AutomatedHouseTemperatureController(currentLocalTimeFinder, thermostatMessager);
	}

	@Test
	public void setsHouseTempTo64IfItIsMidnightOnWeekdays() {
		DateTime mondayNightMidnight = new DateTime(2018, 11, 5, 0, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = mondayNightMidnight.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempAtMidnightOnWeekends() {
		DateTime fridayNightMidnight = new DateTime(2018, 11, 3, 0, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(fridayNightMidnight);
		controller.setTempAtTimeTriggers();

		DateTime saturdayNightMidnight = fridayNightMidnight.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturdayNightMidnight);
		controller.setTempAtTimeTriggers();

		verifyZeroInteractions(thermostatMessager);
	}


	@Test
	public void doesNotSetHouseTempTo64IfItIsElevenFiftyNineOnWeekdays() {
		DateTime mondayNightMidnight = new DateTime(2018, 11, 5, 0, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime sundayNightElevenFiftyNine = mondayNightMidnight.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = sundayNightElevenFiftyNine.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verifyZeroInteractions(thermostatMessager);
	}

	@Test
	public void setsHouseTempTo64IfItIsMidnightOhNineOnWeekdays() {
		DateTime mondayNightMidnightTen = new DateTime(2018, 11, 5, 0, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime mondayNightMidnightOhNine = mondayNightMidnightTen.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnightOhNine = mondayNightMidnightOhNine.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnightOhNine);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempTo64IfItIsMidnightTenOnWeekdays() {
		DateTime mondayNightMidnightTen = new DateTime(2018, 11, 5, 0, 10, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight10 = mondayNightMidnightTen.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight10);
			controller.setTempAtTimeTriggers();
		}
		verifyZeroInteractions(thermostatMessager);
	}

//
//
//
//

	@Test
	public void setsHouseTempTo69IfItIs7amOnWeekdays() {
		DateTime monday7am = new DateTime(2018, 11, 5, 7, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = monday7am.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(69));
	}

	@Test
	public void doesNotSetHouseTempAt7amOnWeekends() {
		DateTime friday7am = new DateTime(2018, 11, 3, 7, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(friday7am);
		controller.setTempAtTimeTriggers();

		DateTime saturday7am = friday7am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday7am);
		controller.setTempAtTimeTriggers();

		verifyZeroInteractions(thermostatMessager);
	}


	@Test
	public void doesNotSetHouseTempTo69IfItIsSixFiftyNineOnWeekdays() {
		DateTime monday7am = new DateTime(2018, 11, 5, 7, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime monday659 = monday7am.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekday659 = monday659.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekday659);
			controller.setTempAtTimeTriggers();
		}
		verifyZeroInteractions(thermostatMessager);
	}

	@Test
	public void setsHouseTempTo69IfItIsSevenOhNineOnWeekdays() {
		DateTime monday710 = new DateTime(2018, 11, 5, 7, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime monday709 = monday710.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekday709 = monday709.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekday709);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(69));
	}

	@Test
	public void doesNotSetHouseTempTo69IfItIsSevenTenOnWeekdays() {
		DateTime monday710 = new DateTime(2018, 11, 5, 7, 10, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekday710 = monday710.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekday710);
			controller.setTempAtTimeTriggers();
		}
		verifyZeroInteractions(thermostatMessager);
	}

}