package com.poorknight.scheduledtasks.timedtemp;

import com.poorknight.house.thermostat.ThermostatMessager;
import com.poorknight.house.thermostat.ThermostatStatus;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.poorknight.house.thermostat.ThermostatStatus.ThermostatMode.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AutomatedHouseTemperatureControllerTest {

	private static final ThermostatStatus STATUS_IN_FURNACE_MODE = new ThermostatStatus(65.5, 65.5, null, FURNACE_MODE);
	private static final ThermostatStatus STATUS_IN_AC_MODE = new ThermostatStatus(65.5, 65.5, null, AC_MODE);
	private static final ThermostatStatus STATUS_IN_NO_MODE = new ThermostatStatus(65.5, 65.5, null, OFF);


	private AutomatedHouseTemperatureController controller;

	@Mock
	private TimeFinder timeFinder;

	@Mock
	private ThermostatMessager thermostatMessager;

	@Before
	public void setUp() {
		controller = new AutomatedHouseTemperatureController(timeFinder, thermostatMessager);
		when(thermostatMessager.requestThermostatStatus()).thenReturn(STATUS_IN_FURNACE_MODE);
	}

	@Test
	public void setsHouseTempTo64IfItIsMidnightOnWeekdays() {
		DateTime mondayNightMidnight = new DateTime(2018, 11, 5, 0, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = mondayNightMidnight.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempIfACModeIsOn() {
		when(thermostatMessager.requestThermostatStatus()).thenReturn(STATUS_IN_AC_MODE);

		DateTime mondayNightMidnight = new DateTime(2018, 11, 5, 0, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = mondayNightMidnight.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(0)).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void doesNotSetHouseTempIfNoModeIsOn() {
		when(thermostatMessager.requestThermostatStatus()).thenReturn(STATUS_IN_NO_MODE);

		DateTime mondayNightMidnight = new DateTime(2018, 11, 5, 0, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = mondayNightMidnight.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void doesNotSetHouseTempAtMidnightOnWeekends() {
		DateTime fridayNightMidnight = new DateTime(2018, 11, 3, 0, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(timeFinder.getCurrentLocalTime()).thenReturn(fridayNightMidnight);
		controller.setTempAtTimeTriggers();

		DateTime saturdayNightMidnight = fridayNightMidnight.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(saturdayNightMidnight);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}


	@Test
	public void doesNotSetHouseTempTo64IfItIsElevenFiftyNineOnWeekdays() {
		DateTime mondayNightMidnight = new DateTime(2018, 11, 5, 0, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime sundayNightElevenFiftyNine = mondayNightMidnight.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight = sundayNightElevenFiftyNine.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void setsHouseTempTo64IfItIsMidnightOhNineOnWeekdays() {
		DateTime mondayNightMidnightTen = new DateTime(2018, 11, 5, 0, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime mondayNightMidnightOhNine = mondayNightMidnightTen.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnightOhNine = mondayNightMidnightOhNine.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnightOhNine);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempTo64IfItIsMidnightTenOnWeekdays() {
		DateTime mondayNightMidnightTen = new DateTime(2018, 11, 5, 0, 10, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekdayMidnight10 = mondayNightMidnightTen.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight10);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
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
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekdayMidnight);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(67));
	}

	@Test
	public void doesNotSetHouseTempAt7amOnWeekends() {
		DateTime friday7am = new DateTime(2018, 11, 3, 7, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(timeFinder.getCurrentLocalTime()).thenReturn(friday7am);
		controller.setTempAtTimeTriggers();

		DateTime saturday7am = friday7am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday7am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}


	@Test
	public void doesNotSetHouseTempIfItIsSixFiftyNineOnWeekdays() {
		DateTime monday7am = new DateTime(2018, 11, 5, 7, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime monday659 = monday7am.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekday659 = monday659.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekday659);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void setsHouseTempTo67IfItIsSevenOhNineOnWeekdays() {
		DateTime monday710 = new DateTime(2018, 11, 5, 7, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime monday709 = monday710.minusMillis(1);

		for (int i = 0; i < 5; i++) {
			DateTime weekday709 = monday709.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekday709);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, times(5)).postHeatTargetTemperature(new BigDecimal(67));
	}

	@Test
	public void doesNotSetHouseTempIfItIsSevenTenOnWeekdays() {
		DateTime monday710 = new DateTime(2018, 11, 5, 7, 10, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekday710 = monday710.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekday710);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

//
//
//

	@Test
	public void setsHouseTempTo64IfItIs3amOnWeekends() {
		DateTime saturday3am = new DateTime(2018, 11, 3, 3, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday3am);
		controller.setTempAtTimeTriggers();

		DateTime sunday3am = saturday3am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday3am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempAt3amOnWeekdays() {
		DateTime monday3am = new DateTime(2018, 11, 5, 3, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekday3am = monday3am.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekday3am);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void doesNotSetHouseTempTo64IfItIs259OnWeekends() {
		DateTime saturday3am = new DateTime(2018, 11, 3, 3, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturday259am = saturday3am.minusMillis(1);

		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday259am);
		controller.setTempAtTimeTriggers();

		DateTime sunday259am = saturday259am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday259am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void setsHouseTempTo64IfItIs309OnWeekends() {
		DateTime saturday310am = new DateTime(2018, 11, 3, 3, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturdayNight309am = saturday310am.minusMillis(1);

		when(timeFinder.getCurrentLocalTime()).thenReturn(saturdayNight309am);
		controller.setTempAtTimeTriggers();

		DateTime sunday309am = saturdayNight309am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday309am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(64));
	}

	@Test
	public void doesNotSetHouseTempTo64IfItIs310OnWeekends() {
		DateTime saturday310am = new DateTime(2018, 11, 3, 3, 10, 0, DateTimeZone.forID("America/Detroit"));

		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday310am);
		controller.setTempAtTimeTriggers();

		DateTime sunday310am = saturday310am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday310am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

//
//
//

	@Test
	public void setsHouseTempTo67IfItIs11amOnWeekends() {
		DateTime saturday11am = new DateTime(2018, 11, 3, 11, 0, 0, DateTimeZone.forID("America/Detroit"));
		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday11am);
		controller.setTempAtTimeTriggers();

		DateTime sunday11am = saturday11am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday11am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(67));
	}

	@Test
	public void doesNotSetHouseTempAt11amOnWeekdays() {
		DateTime monday11am = new DateTime(2018, 11, 5, 11, 0, 0, DateTimeZone.forID("America/Detroit"));

		for (int i = 0; i < 5; i++) {
			DateTime weekday11am = monday11am.plusDays(i);
			when(timeFinder.getCurrentLocalTime()).thenReturn(weekday11am);
			controller.setTempAtTimeTriggers();
		}
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void doesNotSetHouseTempIfItIs1059OnWeekends() {
		DateTime saturday11am = new DateTime(2018, 11, 3, 11, 0, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturday1059am = saturday11am.minusMillis(1);

		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday1059am);
		controller.setTempAtTimeTriggers();

		DateTime sunday1059am = saturday1059am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday1059am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void setsHouseTempTo67IfItIs1109OnWeekends() {
		DateTime saturday1110am = new DateTime(2018, 11, 3, 11, 10, 0, DateTimeZone.forID("America/Detroit"));
		DateTime saturday1109am = saturday1110am.minusMillis(1);

		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday1109am);
		controller.setTempAtTimeTriggers();

		DateTime sunday1109am = saturday1109am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday1109am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, times(2)).postHeatTargetTemperature(new BigDecimal(67));
	}

	@Test
	public void doesNotSetHouseTempTo69IfItIs1110OnWeekends() {
		DateTime saturday1110am = new DateTime(2018, 11, 3, 11, 10, 0, DateTimeZone.forID("America/Detroit"));

		when(timeFinder.getCurrentLocalTime()).thenReturn(saturday1110am);
		controller.setTempAtTimeTriggers();

		DateTime sunday1110am = saturday1110am.plusDays(1);
		when(timeFinder.getCurrentLocalTime()).thenReturn(sunday1110am);
		controller.setTempAtTimeTriggers();

		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}
}