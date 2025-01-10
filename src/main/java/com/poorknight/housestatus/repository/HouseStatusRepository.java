package com.poorknight.housestatus.repository;

import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.house.thermostat.ThermostatStatus;
import com.poorknight.house.thermostat.ThermostatStatus.FurnaceState;
import com.poorknight.house.thermostat.ThermostatStatus.ThermostatMode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class HouseStatusRepository {

	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormat.forPattern(DATE_TIME_PATTERN);

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
			String insertStatement = buildInsertStatement(currentUtcTime, currentLocalTime, thermostatStatus, weatherStatus);
			statement.execute(insertStatement);

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
		String thermostatMode = thermostatStatus.getThermostatMode().toString();

		Double externalTemp = weatherStatus.getTempFahrenheit();
		Double windSpeed = weatherStatus.getWindSpeedMph();
		Double humidity = weatherStatus.getHumidityPercent();
		Double pressure = weatherStatus.getPressureHPa();

		String formatString = "INSERT INTO my_house.house_status(" +
				"time_utc, time_local, house_temp, temp_setting, furnace_state, " +
				"external_temp_f, external_wind_speed_mph, external_humidity_percent, external_pressure_hpa, thermostat_mode) " +
				"values ('%s', '%s', %5.2f, %5.2f, '%s', %5.2f, %5.2f, %5.2f, %6.2f, '%s')";
		return String.format(formatString, utcTime, localTime, temp, tempSetting, furnaceState, externalTemp, windSpeed, humidity, pressure, thermostatMode);
	}

	public List<HouseDataPoint> retrieveHouseStatusFrom(DateTime startTimeUtc, DateTime endTimeUtc) {
		String startTimeString = startTimeUtc.toString(DATE_TIME_PATTERN);
		String endTimeString = endTimeUtc.toString(DATE_TIME_PATTERN);
		String query = "SELECT * FROM my_house.house_status WHERE time_utc >= '" + startTimeString + "' AND time_utc <= '" + endTimeString + "'";

		Connection connection = null;
		Statement statement = null;
		try {
			connection = databaseConnector.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			List<HouseDataPoint> dataPoints = new LinkedList<>();
			while (resultSet.next()) {
				HouseDataPoint houseDataPoint = buildHouseDataPointFromCurrentResult(resultSet);
				dataPoints.add(houseDataPoint);
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

	private HouseDataPoint buildHouseDataPointFromCurrentResult(ResultSet resultSet) throws SQLException {
		DateTime localTime = DATE_TIME_FORMAT.parseDateTime(resultSet.getString("time_local"));
		DateTime utcTime = DATE_TIME_FORMAT.parseDateTime(resultSet.getString("time_utc"));

		Double houseTemp = resultSet.getDouble("house_temp");
		Double tempSetting = resultSet.getDouble("temp_setting");
		FurnaceState furnaceState = FurnaceState.valueOf(resultSet.getString("furnace_state"));
		String thermostat_mode = resultSet.getString("thermostat_mode");
		ThermostatMode thermostatMode = thermostat_mode == null ? null : ThermostatMode.valueOf(thermostat_mode);
		ThermostatStatus thermostatStatus = new ThermostatStatus(houseTemp, tempSetting, furnaceState, thermostatMode);

		Double tempFahrenheit = resultSet.getDouble("external_temp_f");
		Double windSpeedMph = resultSet.getDouble("external_wind_speed_mph");
		Double humidityPercent = resultSet.getDouble("external_humidity_percent");
		Double pressureHPa = resultSet.getDouble("external_pressure_hpa");
		WeatherStatus weatherStatus = new WeatherStatus(tempFahrenheit, windSpeedMph, humidityPercent, pressureHPa);

		return new HouseDataPoint(localTime, utcTime, thermostatStatus, weatherStatus);
	}
}
