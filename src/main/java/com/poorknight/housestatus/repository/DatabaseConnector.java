package com.poorknight.housestatus.repository;

import com.poorknight.server.settings.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

	private DatabaseConnectionParameters databaseConnectionParameters;

	public DatabaseConnector() {
		String jdbcString = Environment.getEnvironmentVariable("JDBC_CONNECTION_STRING");
		String user = Environment.getEnvironmentVariable("DB_USER");
		String password = Environment.getEnvironmentVariable("DB_PASSWORD");

		Properties connectionProps = new Properties();
		connectionProps.setProperty("user", user);
		connectionProps.setProperty("password", password);
		this.databaseConnectionParameters = new DatabaseConnectionParameters(jdbcString, connectionProps);
	}

	public DatabaseConnector(DatabaseConnectionParameters databaseConnectionParameters) {
		this.databaseConnectionParameters = databaseConnectionParameters;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(databaseConnectionParameters.getJdbcUrl(), databaseConnectionParameters.getConnectionProps());
	}

	public DatabaseConnectionParameters getDatabaseConnectionParameters() {
		return this.databaseConnectionParameters;
	}
}
