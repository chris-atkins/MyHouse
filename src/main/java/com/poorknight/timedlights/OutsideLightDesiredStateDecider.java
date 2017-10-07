package com.poorknight.timedlights;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.ws.rs.core.MediaType;

import static com.poorknight.timedlights.DesiredState.OFF;
import static com.poorknight.timedlights.DesiredState.ON;
import static org.joda.time.DateTimeZone.UTC;

public class OutsideLightDesiredStateDecider {

	private String sunUrl = "https://api.sunrise-sunset.org/json?lat=42.5141149&lng=-83.2139536&formatted=0";

	public DesiredState findDesiredState() {

		Logger.getLogger(this.getClass()).info("ABOUT TO CALL SUNRISE/SUNSET TIME INFO");

		final JsonNode sunTimeInfo = retrieveSunTimeInfo();

		Logger.getLogger(this.getClass()).info(sunTimeInfo.toString());

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
		final DateTime now = DateTime.now(UTC);
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
}
