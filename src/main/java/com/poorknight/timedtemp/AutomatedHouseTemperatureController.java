package com.poorknight.timedtemp;


import com.poorknight.thermostat.ThermostatMessager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;

public class AutomatedHouseTemperatureController {

	private final CurrentLocalTimeFinder currentLocalTimeFinder;

	private ThermostatMessager thermostatMessager;

	public AutomatedHouseTemperatureController(CurrentLocalTimeFinder currentLocalTimeFinder, ThermostatMessager thermostatMessager) {
		this.currentLocalTimeFinder = currentLocalTimeFinder;
		this.thermostatMessager = thermostatMessager;
	}


	public void setTempAtTimeTriggers() {
		DateTime currentLocalTime = currentLocalTimeFinder.getCurrentLocalTime();

		if (itIsAWeekday(currentLocalTime)) {
			if (itIsWithin10MinutesFromMidnight(currentLocalTime)) {
				thermostatMessager.postHeatTargetTemperature(new BigDecimal(64));
			}
			if (itIsWithin10MinutesFrom7am(currentLocalTime)) {
				thermostatMessager.postHeatTargetTemperature(new BigDecimal(69));
			}
		}
	}

	private boolean itIsAWeekday(DateTime currentLocalTime) {
		int dayOfWeek = currentLocalTime.getDayOfWeek();
		if (dayOfWeek == DateTimeConstants.SATURDAY || dayOfWeek == DateTimeConstants.SUNDAY) {
			return false;
		}
		return true;
	}

	private boolean itIsWithin10MinutesFromMidnight(DateTime currentLocalTime) {
		int hourOfDay = currentLocalTime.getHourOfDay();
		int minuteOfDay = currentLocalTime.getMinuteOfHour();
		if (hourOfDay == 0 && minuteOfDay < 10) {
			return true;
		}
		return false;
	}

	private boolean itIsWithin10MinutesFrom7am(DateTime currentLocalTime) {
		int hourOfDay = currentLocalTime.getHourOfDay();
		int minuteOfDay = currentLocalTime.getMinuteOfHour();
		if (hourOfDay == 7 && minuteOfDay < 10) {
			return true;
		}
		return false;
	}
}
