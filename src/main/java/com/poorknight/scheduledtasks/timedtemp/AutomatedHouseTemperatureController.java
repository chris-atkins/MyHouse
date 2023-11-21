package com.poorknight.scheduledtasks.timedtemp;


import com.poorknight.house.thermostat.ThermostatMessager;
import com.poorknight.house.thermostat.ThermostatStatus;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;

import static com.poorknight.house.thermostat.ThermostatStatus.ThermostatMode.FURNACE_MODE;

public class AutomatedHouseTemperatureController {

	private final TimeFinder timeFinder;

	private ThermostatMessager thermostatMessager;

	public AutomatedHouseTemperatureController(TimeFinder timeFinder, ThermostatMessager thermostatMessager) {
		this.timeFinder = timeFinder;
		this.thermostatMessager = thermostatMessager;
	}

	public void setTempAtTimeTriggers() {
		if (this.houseIsNotInFurnaceMode()) {
			return;
		}

		DateTime currentLocalTime = timeFinder.getCurrentLocalTime();

		if (itIsAWeekday(currentLocalTime)) {
			lowerHeatBetweenMidnightAnd740am(currentLocalTime);
		} else {
			lowerHeatBetween3amAnd11am(currentLocalTime);
		}
	}

	private boolean houseIsNotInFurnaceMode() {
		ThermostatStatus thermostatStatus = this.thermostatMessager.requestThermostatStatus();

		if (thermostatStatus.getThermostatMode() == FURNACE_MODE) {
			return false;
		}
		return true;
	}

	private boolean itIsAWeekday(DateTime currentLocalTime) {
		int dayOfWeek = currentLocalTime.getDayOfWeek();
		if (dayOfWeek == DateTimeConstants.SATURDAY || dayOfWeek == DateTimeConstants.SUNDAY) {
			return false;
		}
		return true;
	}

	private void lowerHeatBetweenMidnightAnd740am(DateTime currentLocalTime) {
		if (itIsWithin10MinutesFromMidnight(currentLocalTime)) {
			thermostatMessager.setHeatModeOnWithTargetTemp(new BigDecimal(64));
		}
		if (itIsWithin10MinutesFrom740am(currentLocalTime)) {
			thermostatMessager.setHeatModeOnWithTargetTemp(new BigDecimal(67));
		}
	}

	private void lowerHeatBetween3amAnd11am(DateTime currentLocalTime) {
		if (itIsWithin10MinutesFrom3am(currentLocalTime)) {
			thermostatMessager.setHeatModeOnWithTargetTemp(new BigDecimal(64));
		}
		if (itIsWithin10MinutesFrom11am(currentLocalTime)) {
			thermostatMessager.setHeatModeOnWithTargetTemp(new BigDecimal(67));
		}
	}

	private boolean itIsWithin10MinutesFromMidnight(DateTime currentLocalTime) {
		return isWithin10MinutesOfHourAndMinute(currentLocalTime, 0, 0);
	}

	private boolean itIsWithin10MinutesFrom740am(DateTime currentLocalTime) {
		return isWithin10MinutesOfHourAndMinute(currentLocalTime, 7, 40);
	}

	private boolean itIsWithin10MinutesFrom3am(DateTime currentLocalTime) {
		return isWithin10MinutesOfHourAndMinute(currentLocalTime, 3, 0);
	}

	private boolean itIsWithin10MinutesFrom11am(DateTime currentLocalTime) {
		return isWithin10MinutesOfHourAndMinute(currentLocalTime, 11, 0);
	}

	private boolean isWithin10MinutesOfHourAndMinute(DateTime currentLocalTime, int targetHour, int targetMinute) {
		int hourOfDay = currentLocalTime.getHourOfDay();
		int minuteOfHour = currentLocalTime.getMinuteOfHour();
		if (hourOfDay == targetHour &&  minuteOfHour >= targetMinute && minuteOfHour < (targetMinute + 10)) {
			return true;
		}
		return false;
	}
}
