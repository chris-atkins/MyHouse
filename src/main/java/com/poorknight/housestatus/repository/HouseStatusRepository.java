package com.poorknight.housestatus.repository;

import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.thermostat.ThermostatStatus;
import com.poorknight.thermostat.ThermostatStatus.FurnaceState;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class HouseStatusRepository {

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private DatabaseConnector databaseConnector;

	public HouseStatusRepository(DatabaseConnector databaseConnector) {
		this.databaseConnector = databaseConnector;
	}

	public void addHouseStatus(DateTime currentUtcTime, DateTime currentLocalTime, ThermostatStatus thermostatStatus, WeatherStatus weatherStatus) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = databaseConnector.getConnection();
			statement = connection.createStatement();
			statement.execute(buildInsertStatement(currentUtcTime, currentLocalTime, thermostatStatus, weatherStatus));

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private String buildInsertStatement(DateTime currentUtcTime, DateTime currentLocalTime, ThermostatStatus thermostatStatus, WeatherStatus weatherStatus) {
		String utcTime = currentUtcTime.toString(DATE_TIME_PATTERN);
		String localTime = currentLocalTime.toString(DATE_TIME_PATTERN);
		double temp = thermostatStatus.getHouseTemp();
		double tempSetting = thermostatStatus.getTempSetting();
		String furnaceState = thermostatStatus.getFurnaceState().toString();

		Double externalTemp = weatherStatus.getTempFahrenheit();
		Double windSpeed = weatherStatus.getWindSpeedMph();
		Double humidity = weatherStatus.getHumidityPercent();
		Double pressure = weatherStatus.getPressureHPa();

		String formatString = "INSERT INTO HOUSE_STATUS(" +
				"TIME_UTC, TIME_LOCAL, HOUSE_TEMP, TEMP_SETTING, FURNACE_STATE, " +
				"EXTERNAL_TEMP_F, EXTERNAL_WIND_SPEED_MPH, EXTERNAL_HUMIDITY_PERCENT, EXTERNAL_PRESSURE_HPA) " +
				"values (\"%s\", \"%s\", %5.2f, %5.2f, \"%s\", %5.2f, %5.2f, %5.2f, %6.2f)";
		return String.format(formatString, utcTime, localTime, temp, tempSetting, furnaceState, externalTemp, windSpeed, humidity, pressure);
	}

	public List<HouseDataPoint> retrieveHouseStatusFrom(DateTime startTimeUtc, DateTime endTimeUtc) {
		String startTimeString = startTimeUtc.toString(DATE_TIME_PATTERN);
		String endTimeString = endTimeUtc.toString(DATE_TIME_PATTERN);
		String query = "SELECT * FROM HOUSE_STATUS WHERE TIME_UTC >= \"" + startTimeString + "\" AND TIME_UTC <= \"" + endTimeString + "\"";

		Connection connection = null;
		Statement statement = null;
		try {
			connection = databaseConnector.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			DateTimeFormatter format = DateTimeFormat.forPattern(DATE_TIME_PATTERN);
			List<HouseDataPoint> dataPoints = new LinkedList<>();
			while (resultSet.next()) {
				DateTime localTime = format.parseDateTime(resultSet.getString("TIME_LOCAL"));
				DateTime utcTime = format.parseDateTime(resultSet.getString("TIME_UTC"));

				Double houseTemp = resultSet.getDouble("HOUSE_TEMP");
				Double tempSetting = resultSet.getDouble("TEMP_SETTING");
				FurnaceState furnaceState = FurnaceState.valueOf(resultSet.getString("FURNACE_STATE"));
				ThermostatStatus thermostatStatus = new ThermostatStatus(houseTemp, tempSetting, furnaceState);

				Double tempFahrenheit = resultSet.getDouble("EXTERNAL_TEMP_F");
				Double windSpeedMph = resultSet.getDouble("EXTERNAL_WIND_SPEED_MPH");
				Double humidityPercent = resultSet.getDouble("EXTERNAL_HUMIDITY_PERCENT");
				Double pressureHPa = resultSet.getDouble("EXTERNAL_PRESSURE_HPA");
				WeatherStatus weatherStatus = new WeatherStatus(tempFahrenheit, windSpeedMph, humidityPercent, pressureHPa);

				dataPoints.add(new HouseDataPoint(localTime, utcTime, thermostatStatus, weatherStatus));
			}
			resultSet.close();
			return dataPoints;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
