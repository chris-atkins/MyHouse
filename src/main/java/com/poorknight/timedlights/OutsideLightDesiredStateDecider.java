package com.poorknight.timedlights;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ws.rs.core.MediaType;

import static com.poorknight.timedlights.DesiredState.OFF;
import static com.poorknight.timedlights.DesiredState.ON;
import static org.joda.time.DateTimeZone.UTC;

public class OutsideLightDesiredStateDecider {

	private String sunUrl = "http://api.sunrise-sunset.org/json?lat=42.5141149&lng=-83.2139536&formatted=0";

	public DesiredState findDesiredState() {
		final JsonNode sunTimeInfo = retrieveSunTimeInfo();

		if (isDaytime(sunTimeInfo)) {
			return OFF;
		}
		return ON;
	}

	private JsonNode retrieveSunTimeInfo() {
		return Client.create()
				.resource(sunUrl)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(JsonNode.class);
	}

	private boolean isDaytime(JsonNode sunTimeInfo) {
		final DateTime sunrise = findSunrise(sunTimeInfo);
		final DateTime sunset = findSunset(sunTimeInfo);
		final DateTime now = findCurrentUTCTimeAdjustedForDetroitDaylightSavings();

		return sunrise.isBefore(now) && now.isBefore(sunset);
	}

	private DateTime findSunset(final JsonNode response) {
		final JsonNode resultsNode = response.get("results");
		final String sunsetString = resultsNode.get("sunset").asText();
		return new DateTime(sunsetString);
	}

	private DateTime findSunrise(final JsonNode response) {
		final JsonNode resultsNode = response.get("results");
		final String sunriseString = resultsNode.get("sunrise").asText();
		return new DateTime(sunriseString);
	}

	private DateTime findCurrentUTCTimeAdjustedForDetroitDaylightSavings() {
		DateTimeZone zone = DateTimeZone.forID("America/Detroit");
		DateTime utcNow = DateTime.now(UTC);

		int fiveHours = 18000000;
		int offset = zone.getOffset(utcNow) + fiveHours;
		return utcNow.minus(offset);
	}
}
