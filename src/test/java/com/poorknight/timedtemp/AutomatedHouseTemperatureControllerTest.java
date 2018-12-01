package com.poorknight.timedtemp;

import com.poorknight.thermostat.ThermostatMessager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Ignore;
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
	public void setsHouseTempTo67IfItIs7amOnWeekdays() {
		DateTime monday7am = new DateTime(2018, 11, 5, 7, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = monday7am.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(67));
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
	public void doesNotSetHouseTempIfItIsSixFiftyNineOnWeekdays() {
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
	public void setsHouseTempTo67IfItIsSevenOhNineOnWeekdays() {
		DateTime monday710 = new DateTime(2018, 11, 5, 7, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime monday709 = monday710.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekday709 = monday709.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekday709);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(67));
	}

	@Test
	public void doesNotSetHouseTempIfItIsSevenTenOnWeekdays() {
		DateTime monday710 = new DateTime(2018, 11, 5, 7, 10, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekday710 = monday710.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekday710);
			controller.setTempAtTimeTriggers();
		}
		verifyZeroInteractions(thermostatMessager);
	}

//
//
//

	@Test
	public void setsHouseTempTo64IfItIs3amOnWeekends() {
		DateTime saturday3am = new DateTime(2018, 11, 3, 3, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday3am);
		controller.setTempAtTimeTriggers();

		DateTime sunday3am = saturday3am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday3am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempAt3amOnWeekdays() {
		DateTime monday3am = new DateTime(2018, 11, 5, 3, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekday3am = monday3am.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekday3am);
			controller.setTempAtTimeTriggers();
		}
		verifyZeroInteractions(thermostatMessager);
	}

	@Test
	public void doesNotSetHouseTempTo64IfItIs259OnWeekends() {
		DateTime saturday3am = new DateTime(2018, 11, 3, 3, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturday259am = saturday3am.minusMillis(1);

		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday259am);
		controller.setTempAtTimeTriggers();

		DateTime sunday259am = saturday259am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday259am);
		controller.setTempAtTimeTriggers();

		verifyZeroInteractions(thermostatMessager);
	}

	@Test
	public void setsHouseTempTo64IfItIs309OnWeekends() {
		DateTime saturday310am = new DateTime(2018, 11, 3, 3, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturdayNight309am = saturday310am.minusMillis(1);

		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturdayNight309am);
		controller.setTempAtTimeTriggers();

		DateTime sunday309am = saturdayNight309am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday309am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempTo64IfItIs310OnWeekends() {
		DateTime saturday310am = new DateTime(2018, 11, 3, 3, 10, 0, DateTimeZone.forID("America/Detroit"));

		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday310am);
		controller.setTempAtTimeTriggers();

		DateTime sunday310am = saturday310am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday310am);
		controller.setTempAtTimeTriggers();

		verifyZeroInteractions(thermostatMessager);
	}

//
//
//

	@Test
	public void setsHouseTempTo67IfItIs11amOnWeekends() {
		DateTime saturday11am = new DateTime(2018, 11, 3, 11, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday11am);
		controller.setTempAtTimeTriggers();

		DateTime sunday11am = saturday11am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday11am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(67));
	}

	@Test
	public void doesNotSetHouseTempAt11amOnWeekdays() {
		DateTime monday11am = new DateTime(2018, 11, 5, 11, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekday11am = monday11am.plusDays(i);
			when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(weekday11am);
			controller.setTempAtTimeTriggers();
		}
		verifyZeroInteractions(thermostatMessager);
	}

	@Test
	public void doesNotSetHouseTempIfItIs1059OnWeekends() {
		DateTime saturday11am = new DateTime(2018, 11, 3, 11, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturday1059am = saturday11am.minusMillis(1);

		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday1059am);
		controller.setTempAtTimeTriggers();

		DateTime sunday1059am = saturday1059am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday1059am);
		controller.setTempAtTimeTriggers();

		verifyZeroInteractions(thermostatMessager);
	}

	@Test
	public void setsHouseTempTo67IfItIs1109OnWeekends() {
		DateTime saturday1110am = new DateTime(2018, 11, 3, 11, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturday1109am = saturday1110am.minusMillis(1);

		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday1109am);
		controller.setTempAtTimeTriggers();

		DateTime sunday1109am = saturday1109am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday1109am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(67));
	}

	@Test
	public void doesNotSetHouseTempTo69IfItIs1110OnWeekends() {
		DateTime saturday1110am = new DateTime(2018, 11, 3, 11, 10, 0, DateTimeZone.forID("America/Detroit"));

		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(saturday1110am);
		controller.setTempAtTimeTriggers();

		DateTime sunday1110am = saturday1110am.plusDays(1);
		when(currentLocalTimeFinder.getCurrentLocalTime()).thenReturn(sunday1110am);
		controller.setTempAtTimeTriggers();

		verifyZeroInteractions(thermostatMessager);
	}
}