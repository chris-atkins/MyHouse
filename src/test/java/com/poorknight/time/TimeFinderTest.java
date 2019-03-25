package com.poorknight.time;

import com.poorknight.time.TimeFinder.UtcTimeRange;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
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

	@Test
	public void returnsUtcRangeForLocalDate() {
		LocalDate date = new LocalDate(2019, 3,24);
		UtcTimeRange timeRange = new TimeFinder().getUtcRangeForLocalDay(date);

		assertThat(timeRange.getStartTime().toString("yyyy-MM-dd HH:mm:ss")).isEqualTo("2019-03-24 04:00:00");
		assertThat(timeRange.getEndTime().toString("yyyy-MM-dd HH:mm:ss")).isEqualTo("2019-03-25 03:59:59");
	}

	@Test
	public void returnsUtcRangeForLocalDateWithDaylightSavings() {
		LocalDate date = new LocalDate(2019, 11,24);
		UtcTimeRange timeRange = new TimeFinder().getUtcRangeForLocalDay(date);

		assertThat(timeRange.getStartTime().toString("yyyy-MM-dd HH:mm:ss")).isEqualTo("2019-11-24 05:00:00");
		assertThat(timeRange.getEndTime().toString("yyyy-MM-dd HH:mm:ss")).isEqualTo("2019-11-25 04:59:59");
	}
}