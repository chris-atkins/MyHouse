package com.poorknight.time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.ZoneId;
import java.util.TimeZone;

public class TimeFinder {

	public DateTime getCurrentLocalTime() {
		DateTime now = DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone(ZoneId.of("America/Detroit"))));
		return now;
	}

	public DateTime getUtcTimeFromLocalTime(DateTime localTime) {
		return new DateTime(localTime, DateTimeZone.UTC);
	}
}
