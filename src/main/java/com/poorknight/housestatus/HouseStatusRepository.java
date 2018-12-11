package com.poorknight.housestatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HouseStatusRepository {

	private DatabaseConnector databaseConnector;

	public HouseStatusRepository(DatabaseConnector databaseConnector) {
		this.databaseConnector = databaseConnector;
	}

	public void addHouseStatus(HouseStatus houseStatus, WeatherStatus weatherStatus) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = databaseConnector.getConnection();
			statement = connection.createStatement();
			statement.execute(buildInsertStatement(houseStatus, weatherStatus));

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private String buildInsertStatement(HouseStatus houseStatus, WeatherStatus weatherStatus) {
		String utcTime = houseStatus.getUtcTime().toString("yyyy-MM-dd HH:mm:ss");
		String localTime = houseStatus.getLocalTime().toString("yyyy-MM-dd HH:mm:ss");
		double temp = houseStatus.getHouseTemp();
		double tempSetting = houseStatus.getTempSetting();
		String furnaceState = houseStatus.getFurnaceState();

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
}
