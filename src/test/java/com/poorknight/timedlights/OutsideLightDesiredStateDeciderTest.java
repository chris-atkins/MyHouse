package com.poorknight.timedlights;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Client.class, DateTime.class})
public class OutsideLightDesiredStateDeciderTest {

	private final static String url =  "http://api.sunrise-sunset.org/json?lat=42.5141149&lng=-83.2139536&formatted=0";

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	private WebResource.Builder builder;


	private OutsideLightDesiredStateDecider decider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Client.class);
		PowerMockito.mockStatic(DateTime.class);
		PowerMockito.when(Client.create()).thenReturn(client);
		PowerMockito.when(client.resource(url)).thenReturn(webResource);

		decider = new OutsideLightDesiredStateDecider();
	}

	@Test
	public void returnsStateON_IfBetweenSunsetAndSunrise() throws Exception {
		when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.get(JsonNode.class)).thenReturn(buildResponse("2017-12-05T11:35:30+00:00", "2017-12-05T11:35:28+00:00"));
		PowerMockito.when(DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2017-12-05T11:35:29+00:00"));

		final DesiredState desiredState = decider.findDesiredState();

		assertThat(desiredState).isEqualTo(DesiredState.ON);
	}

	@Test
	public void returnsStateOFF_IfBetweenSunriseAndSunset() throws Exception {
		when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.get(JsonNode.class)).thenReturn(buildResponse("2017-12-05T11:35:00+00:00", "2017-12-05T11:35:02+00:00"));
		PowerMockito.when(DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2017-12-05T11:35:01+00:00"));

		final DesiredState desiredState = decider.findDesiredState();

		assertThat(desiredState).isEqualTo(DesiredState.OFF);
	}

	@Test
	public void handlesDaylightSavingsTime() throws Exception {
		when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.get(JsonNode.class)).thenReturn(buildResponse("2018-03-11T11:35:00+00:00", "2018-03-11T11:35:02+00:00"));

		PowerMockito.when(DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2018-03-11T11:35:01+00:00"));
		DesiredState desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2018-03-11T10:35:01+00:00"));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);


		when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
		when(builder.get(JsonNode.class)).thenReturn(buildResponse("2018-11-05T11:35:00+00:00", "2018-11-05T11:35:02+00:00"));

		PowerMockito.when(DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2018-11-05T11:35:01+00:00"));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2018-11-05T10:35:01+00:00"));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

//		Daylight saving time 2018 in Michigan began at 2:00 AM on
//		Sunday, March 11

//		and ends at 2:00 AM on
//		Sunday, November 4
	}

	private JsonNode buildResponse(final String sunrise, final String sunset) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final String response =
				"{\n" +
				"    \"results\": {\n" +
				"        \"sunrise\": \"" + sunrise + "\",\n" +
				"        \"sunset\": \"" + sunset + "\",\n" +
				"        \"solar_noon\": \"2017-10-05T17:20:59+00:00\",\n" +
				"        \"day_length\": 41461,\n" +
				"        \"civil_twilight_begin\": \"2017-10-05T11:07:27+00:00\",\n" +
				"        \"civil_twilight_end\": \"2017-10-05T23:34:30+00:00\",\n" +
				"        \"nautical_twilight_begin\": \"2017-10-05T10:34:52+00:00\",\n" +
				"        \"nautical_twilight_end\": \"2017-10-06T00:07:05+00:00\",\n" +
				"        \"astronomical_twilight_begin\": \"2017-10-05T10:02:04+00:00\",\n" +
				"        \"astronomical_twilight_end\": \"2017-10-06T00:39:54+00:00\"\n" +
				"    },\n" +
				"    \"status\": \"OK\"\n" +
				"}";
		return mapper.readTree(response);
	}
}