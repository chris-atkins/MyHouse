package com.poorknight.time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.time.ZoneId;
import java.util.Objects;
import java.util.TimeZone;

public class TimeFinder {

	public DateTime getCurrentLocalTime() {
		DateTime now = DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone(ZoneId.of("America/Detroit"))));
		return now;
	}

	public DateTime getUtcTimeFromLocalTime(DateTime localTime) {
		return new DateTime(localTime, DateTimeZone.UTC);
	}

	public UtcTimeRange getUtcRangeForLocalDay(LocalDate date) {
		DateTime houseLocalMidnight = date.toDateTime(LocalTime.MIDNIGHT, DateTimeZone.forID("America/Detroit"));
		DateTime startTimeUtc = houseLocalMidnight.toDateTime(DateTimeZone.UTC);
		DateTime endTimeUtc = startTimeUtc.plusDays(1).minusMillis(1);

		return new UtcTimeRange(startTimeUtc, endTimeUtc);
	}

	public static class UtcTimeRange {
		private DateTime startTime;
		private DateTime endTime;

		private UtcTimeRange(DateTime startTime, DateTime endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public DateTime getStartTime() {
			return startTime;
		}

		public DateTime getEndTime() {
			return endTime;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			UtcTimeRange that = (UtcTimeRange) o;
			return Objects.equals(startTime, that.startTime) &&
					Objects.equals(endTime, that.endTime);
		}

		@Override
		public int hashCode() {
			return Objects.hash(startTime, endTime);
		}

		@Override
		public String toString() {
			return "UtcTimeRange{" +
					"startTime=" + startTime +
					", endTime=" + endTime +
					'}';
		}
	}
}
