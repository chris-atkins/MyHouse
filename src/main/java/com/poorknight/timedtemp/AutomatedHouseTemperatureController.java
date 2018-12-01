package com.poorknight.timedtemp;


import com.poorknight.thermostat.ThermostatMessager;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;

public class AutomatedHouseTemperatureController {

	private final TimeFinder timeFinder;

	private ThermostatMessager thermostatMessager;

	public AutomatedHouseTemperatureController(TimeFinder timeFinder, ThermostatMessager thermostatMessager) {
		this.timeFinder = timeFinder;
		this.thermostatMessager = thermostatMessager;
	}

	public void setTempAtTimeTriggers() {
		DateTime currentLocalTime = timeFinder.getCurrentLocalTime();

		if (itIsAWeekday(currentLocalTime)) {
			lowerHeatBetweenMidnightAnd7am(currentLocalTime);
		} else {
			lowerHeatBetween3amAnd11am(currentLocalTime);
		}
	}

	private boolean itIsAWeekday(DateTime currentLocalTime) {
		int dayOfWeek = currentLocalTime.getDayOfWeek();
		if (dayOfWeek == DateTimeConstants.SATURDAY || dayOfWeek == DateTimeConstants.SUNDAY) {
			return false;
		}
		return true;
	}

	private void lowerHeatBetweenMidnightAnd7am(DateTime currentLocalTime) {
		if (itIsWithin10MinutesFromMidnight(currentLocalTime)) {
			thermostatMessager.postHeatTargetTemperature(new BigDecimal(64));
		}
		if (itIsWithin10MinutesFrom7am(currentLocalTime)) {
			thermostatMessager.postHeatTargetTemperature(new BigDecimal(67));
		}
	}

	private void lowerHeatBetween3amAnd11am(DateTime currentLocalTime) {
		if (itIsWithin10MinutesFrom3am(currentLocalTime)) {
			thermostatMessager.postHeatTargetTemperature(new BigDecimal(64));
		}
		if (itIsWithin10MinutesFrom11am(currentLocalTime)) {
			thermostatMessager.postHeatTargetTemperature(new BigDecimal(67));
		}
	}

	private boolean itIsWithin10MinutesFromMidnight(DateTime currentLocalTime) {
		return isWithin10MinutesOfHour(currentLocalTime, 0);
	}

	private boolean itIsWithin10MinutesFrom7am(DateTime currentLocalTime) {
		return isWithin10MinutesOfHour(currentLocalTime, 7);
	}

	private boolean itIsWithin10MinutesFrom3am(DateTime currentLocalTime) {
		return isWithin10MinutesOfHour(currentLocalTime, 3);
	}

	private boolean itIsWithin10MinutesFrom11am(DateTime currentLocalTime) {
		return isWithin10MinutesOfHour(currentLocalTime, 11);
	}

	private boolean isWithin10MinutesOfHour(DateTime currentLocalTime, int i) {
		int hourOfDay = currentLocalTime.getHourOfDay();
		int minuteOfHour = currentLocalTime.getMinuteOfHour();
		if (hourOfDay == i && minuteOfHour < 10) {
			return true;
		}
		return false;
	}
}
