package com.poorknight.scheduledtasks.timedlights;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import javax.ws.rs.core.MediaType;

import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;
import static org.joda.time.DateTimeZone.UTC;

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
