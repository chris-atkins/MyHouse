package com.poorknight.time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.ZoneId;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class TimeFinderTest {

	@Test
	public void returnsUtcTimeForGivenLocalTime() {
		DateTime localTime = new DateTime(2018, 12, 1, 12, 56, 32, DateTimeZone.forTimeZone(TimeZone.getTimeZone(ZoneId.of("America/Detroit"))));

		DateTime utcTime = new TimeFinder().getUtcTimeFromLocalTime(localTime);

		assertThat(utcTime.toString("yyyy-MM-dd HH:mm:ss")).isEqualTo("2018-12-01 17:56:32");
	}
}