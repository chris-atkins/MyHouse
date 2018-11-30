package com.poorknight.housestatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

	private MySqlConnectionParameters mysqlConnectionParameters;

	public DatabaseConnector(MySqlConnectionParameters mysqlConnectionParameters) {
		this.mysqlConnectionParameters = mysqlConnectionParameters;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(mysqlConnectionParameters.getJdbcUrl(), mysqlConnectionParameters.getConnectionProps());
	}
}
