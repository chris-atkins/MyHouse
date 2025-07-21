package com.poorknight.housestatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.housestatus.weather.WeatherRetriever;
import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.server.settings.Environment;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherRetrieverTest {

	private final static String url =  "https://api.openweathermap.org/data/2.5/weather?id=5010636&units=imperial&APPID=12345";

	private WeatherRetriever weatherRetriever = new WeatherRetriever();;

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	private WebResource.Builder builder;

	@Test
	public void returnsCorrectWeather() throws Exception {
		try (MockedStatic<Client> mockedClient = mockStatic(Client.class)) {
			try (MockedStatic<Environment> mockedEnvironment = mockStatic(Environment.class)) {

				mockedEnvironment.when(() -> Environment.getEnvironmentVariable("WEATHER_APP_ID")).thenReturn("12345");

				mockedClient.when(Client::create).thenReturn(client);
				when(client.resource(url)).thenReturn(webResource);
				when(webResource.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
				JsonNode response = buildResponse(1.2, 3.4, 5.6, 7.8);
				when(builder.get(JsonNode.class)).thenReturn(response);

				WeatherStatus currentWeather = weatherRetriever.findCurrentWeather();

				assertThat(currentWeather.getTempFahrenheit()).isEqualTo(1.2);
				assertThat(currentWeather.getWindSpeedMph()).isEqualTo(3.4);
				assertThat(currentWeather.getHumidityPercent()).isEqualTo(5.6);
				assertThat(currentWeather.getPressureHPa()).isEqualTo(7.8);
			}
		}
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