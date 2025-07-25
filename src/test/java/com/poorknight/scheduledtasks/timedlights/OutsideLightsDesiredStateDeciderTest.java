package com.poorknight.scheduledtasks.timedlights;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OutsideLightsDesiredStateDeciderTest {

	private final static String url =  "http://api.sunrise-sunset.org/json?lat=42.5141149&lng=-83.2139536&formatted=0";

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	private WebResource.Builder builder;


	private OutsideLightsDesiredStateDecider decider = new OutsideLightsDesiredStateDecider();

	@BeforeEach
	public void setup() {
		when(client.resource(url)).thenReturn(webResource);
	}

	@Test
	public void returnsStateON_IfBetweenSunsetAndSunrise() throws Exception {
		try(MockedStatic<Client> mockedClient = Mockito.mockStatic(Client.class)) {
			try(MockedStatic<DateTime> mockedDateTime = Mockito.mockStatic(DateTime.class)) {

				mockedClient.when(Client::create).thenReturn(client);

				when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
				when(builder.get(JsonNode.class)).thenReturn(buildResponse("2017-12-05T11:35:30+00:00", "2017-12-05T11:35:28+00:00"));
				mockedDateTime.when(() -> DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2017-12-05T11:35:29+00:00"));

				final DesiredState desiredState = decider.findDesiredState();

				assertThat(desiredState).isEqualTo(DesiredState.ON);
			}
		}
	}

	@Test
	public void returnsStateOFF_IfBetweenSunriseAndSunset() throws Exception {
		try(MockedStatic<Client> mockedClient = Mockito.mockStatic(Client.class)) {
			try (MockedStatic<DateTime> mockedDateTime = Mockito.mockStatic(DateTime.class)) {

				mockedClient.when(Client::create).thenReturn(client);

				when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
				when(builder.get(JsonNode.class)).thenReturn(buildResponse("2017-12-05T11:35:00+00:00", "2017-12-05T11:35:02+00:00"));
				mockedDateTime.when(() -> DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2017-12-05T11:35:01+00:00"));

				final DesiredState desiredState = decider.findDesiredState();

				assertThat(desiredState).isEqualTo(DesiredState.OFF);
			}
		}
	}

	@Test
	public void handlesResultsFromSunsetWebsite_ThatArePrematureNextDayTimes() throws Exception {
		try(MockedStatic<Client> mockedClient = Mockito.mockStatic(Client.class)) {
			try (MockedStatic<DateTime> mockedDateTime = Mockito.mockStatic(DateTime.class)) {

				mockedClient.when(Client::create).thenReturn(client);

				when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
				when(builder.get(JsonNode.class)).thenReturn(buildResponse("2018-07-04T10:01:24.000Z", "2018-07-05T01:13:26.000Z"));

				mockedDateTime.when(() -> DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2018-07-04T00:21:09.240Z"));
				DesiredState desiredState = decider.findDesiredState();
				assertThat(desiredState).isEqualTo(DesiredState.OFF);


				when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
				when(builder.get(JsonNode.class)).thenReturn(buildResponse("2018-07-04T10:01:24.000Z", "2018-07-05T01:13:26.000Z"));

				mockedDateTime.when(() -> DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2018-07-04T01:11:09.195Z"));
				desiredState = decider.findDesiredState();
				assertThat(desiredState).isEqualTo(DesiredState.OFF);


				when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
				when(builder.get(JsonNode.class)).thenReturn(buildResponse("2018-07-04T10:01:24.000Z", "2018-07-05T01:13:26.000Z"));

				PowerMockito.when(DateTime.now(DateTimeZone.UTC)).thenReturn(new DateTime("2018-07-04T01:16:09.328Z"));
				desiredState = decider.findDesiredState();
				assertThat(desiredState).isEqualTo(DesiredState.ON);
			}
		}
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