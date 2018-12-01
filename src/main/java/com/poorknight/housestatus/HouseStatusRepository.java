package com.poorknight.housestatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HouseStatusRepository {

	private DatabaseConnector databaseConnector;

	public HouseStatusRepository(DatabaseConnector databaseConnector) {
		this.databaseConnector = databaseConnector;
	}

	public void addHouseStatus(HouseStatus houseStatus) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = databaseConnector.getConnection();
			statement = connection.createStatement();
			statement.execute(buildInsertStatement(houseStatus));

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

	private String buildInsertStatement(HouseStatus houseStatus) {
		String utcTime = houseStatus.getUtcTime().toString("yyyy-MM-dd HH:mm:ss");
		String localTime = houseStatus.getLocalTime().toString("yyyy-MM-dd HH:mm:ss");
		double temp = houseStatus.getHouseTemp();
		double tempSetting = houseStatus.getTempSetting();
		String furnaceState = houseStatus.getFurnaceState();
		String formatString = "INSERT INTO HOUSE_STATUS(TIME_UTC, TIME_LOCAL, HOUSE_TEMP, TEMP_SETTING, FURNACE_STATE) values (\"%s\", \"%s\", %5.2f, %5.2f, \"%s\")";
		return String.format(formatString, utcTime, localTime, temp, tempSetting, furnaceState);
	}
}
