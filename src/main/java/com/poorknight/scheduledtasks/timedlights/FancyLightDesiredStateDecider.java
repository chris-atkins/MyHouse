package com.poorknight.scheduledtasks.timedlights;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;

public class FancyLightDesiredStateDecider {

	DesiredState findDesiredState() {
		DateTime dateTime = findCurrentDetroitTime();

		int hourOfDay = dateTime.hourOfDay().get();
		if (hourOfDay >= 4 && hourOfDay < 22) {
			return OFF;
		}

		return ON;
	}


	private DateTime findCurrentDetroitTime() {
		return DateTime.now(DateTimeZone.forID("America/Detroit"));
	}

}
