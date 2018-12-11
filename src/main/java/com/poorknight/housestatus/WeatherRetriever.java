package com.poorknight.housestatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.settings.Environment;
import com.sun.jersey.api.client.Client;

import javax.ws.rs.core.MediaType;

public class WeatherRetriever {

	private String url = "http://api.openweathermap.org/data/2.5/weather?id=5010636&units=imperial&APPID=";

	public WeatherStatus findCurrentWeather() {
		JsonNode jsonNode = retrieveWeatherInfo();
		WeatherStatus weatherStatus = translateResponse(jsonNode);
		return weatherStatus;
	}

	private JsonNode retrieveWeatherInfo() {
		String appId = Environment.getEnvironmentVariable("WEATHER_APP_ID");
		return Client.create()
				.resource(url + appId)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(JsonNode.class);
	}

	private WeatherStatus translateResponse(JsonNode jsonNode) {
		JsonNode main = jsonNode.get("main");
		double temp = main.get("temp").asDouble();
		double humidity = main.get("humidity").asDouble();
		double pressure = main.get("pressure").asDouble();

		JsonNode wind = jsonNode.get("wind");
		double windSpeed = wind.get("speed").asDouble();

		return new WeatherStatus(temp, windSpeed, humidity, pressure);
	}
}
