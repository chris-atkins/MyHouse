package com.poorknight.timedlights;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.ws.rs.core.MediaType;

import static com.poorknight.timedlights.DesiredState.OFF;
import static com.poorknight.timedlights.DesiredState.ON;
import static org.joda.time.DateTimeZone.UTC;

public class OutsideLightDesiredStateDecider {

	private static final String sunUrl = "http://api.sunrise-sunset.org/json?lat=42.5141149&lng=-83.2139536&formatted=0";

	DesiredState findDesiredState() {
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
		DateTime sunrise = findSunrise(sunTimeInfo);
		DateTime sunset = findSunset(sunTimeInfo);
		final DateTime now = findCurrentUTCTime();

		if (sunDataIsForTomorrow(now, sunset)) {
			sunrise = sunrise.minus(Days.ONE);
			sunset = sunset.minus(Days.ONE);
		}

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

	private DateTime findCurrentUTCTime() {
		return DateTime.now(UTC);
	}

	private boolean sunDataIsForTomorrow(DateTime now, DateTime sunset) {
		return now.plus(Days.ONE).isBefore(sunset);
	}
}
