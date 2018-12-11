package com.poorknight.housestatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.settings.Environment;
import com.poorknight.timedlights.OutsideLightDesiredStateDecider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Client.class, Environment.class})
public class WeatherRetrieverTest {

	private final static String url =  "http://api.openweathermap.org/data/2.5/weather?id=5010636&units=imperial&APPID=12345";

	private WeatherRetriever weatherRetriever;

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	private WebResource.Builder builder;

	@Before
	public void setup() {
		PowerMockito.mockStatic(Client.class);
		PowerMockito.mockStatic(Environment.class);
		PowerMockito.when(Client.create()).thenReturn(client);
		PowerMockito.when(client.resource(url)).thenReturn(webResource);
		PowerMockito.when(Environment.getEnvironmentVariable("WEATHER_APP_ID")).thenReturn("12345");
		when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);

		weatherRetriever = new WeatherRetriever();
	}

	@Test
	public void returnsCorrectWeather() throws Exception {
		JsonNode response = buildResponse(1.2, 3.4, 5.6, 7.8);
		when(builder.get(JsonNode.class)).thenReturn(response);

		WeatherStatus currentWeather = weatherRetriever.findCurrentWeather();

		assertThat(currentWeather.getTempFahrenheit()).isEqualTo(1.2);
		assertThat(currentWeather.getWindSpeedMph()).isEqualTo(3.4);
		assertThat(currentWeather.getHumidityPercent()).isEqualTo(5.6);
		assertThat(currentWeather.getPressureHPa()).isEqualTo(7.8);
	}

	private JsonNode buildResponse(Double temp, Double windSpeed, Double humidity, Double pressure) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final String response =
				"{\n" +
						"    \"coord\": {\n" +
						"        \"lon\": -83.22,\n" +
						"        \"lat\": 42.47\n" +
						"    },\n" +
						"    \"weather\": [\n" +
						"        {\n" +
						"            \"id\": 721,\n" +
						"            \"main\": \"Haze\",\n" +
						"            \"description\": \"haze\",\n" +
						"            \"icon\": \"50d\"\n" +
						"        },\n" +
						"        {\n" +
						"            \"id\": 701,\n" +
						"            \"main\": \"Mist\",\n" +
						"            \"description\": \"mist\",\n" +
						"            \"icon\": \"50d\"\n" +
						"        }\n" +
						"    ],\n" +
						"    \"base\": \"stations\",\n" +
						"    \"main\": {\n" +
						"        \"temp\": " + temp + ",\n" +
						"        \"pressure\": " + pressure + ",\n" +
						"        \"humidity\": " + humidity + ",\n" +
						"        \"temp_min\": 25.7,\n" +
						"        \"temp_max\": 30.74\n" +
						"    },\n" +
						"    \"visibility\": 6437,\n" +
						"    \"wind\": {\n" +
						"        \"speed\": " + windSpeed + ",\n" +
						"        \"deg\": 210\n" +
						"    },\n" +
						"    \"clouds\": {\n" +
						"        \"all\": 90\n" +
						"    },\n" +
						"    \"dt\": 1544538900,\n" +
						"    \"sys\": {\n" +
						"        \"type\": 1,\n" +
						"        \"id\": 6182,\n" +
						"        \"message\": 0.0046,\n" +
						"        \"country\": \"US\",\n" +
						"        \"sunrise\": 1544532772,\n" +
						"        \"sunset\": 1544565601\n" +
						"    },\n" +
						"    \"id\": 5010636,\n" +
						"    \"name\": \"Southfield\",\n" +
						"    \"cod\": 200\n" +
						"}";
		return mapper.readTree(response);
	}
}