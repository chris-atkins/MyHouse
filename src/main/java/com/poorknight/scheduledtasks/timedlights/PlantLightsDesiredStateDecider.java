package com.poorknight.scheduledtasks.timedlights;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;

public class PlantLightsDesiredStateDecider {

	DesiredState findDesiredState() {
		DateTime dateTime = findCurrentDetroitTime();

		int hourOfDay = dateTime.hourOfDay().get();
		if (hourOfDay < 7 || hourOfDay > 21) {
			return OFF;
		}

		return ON;
	}


	private DateTime findCurrentDetroitTime() {
		return DateTime.now(DateTimeZone.forID("America/Detroit"));
	}

}
