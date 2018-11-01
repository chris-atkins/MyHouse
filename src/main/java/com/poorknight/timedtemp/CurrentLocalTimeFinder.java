package com.poorknight.timedtemp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.ZoneId;
import java.util.TimeZone;

public class CurrentLocalTimeFinder {

	public DateTime getCurrentLocalTime() {
		DateTime now = DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone(ZoneId.of("America/Detroit"))));
		return now;
	}
}
