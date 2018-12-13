package com.poorknight.housestatus.repository;

import com.poorknight.settings.Environment;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

	private MySqlConnectionParameters mysqlConnectionParameters;

	public DatabaseConnector() {
		String jdbcString = Environment.getEnvironmentVariable("JDBC_CONNECTION_STRING");
		String user = Environment.getEnvironmentVariable("DB_USER");
		String password = Environment.getEnvironmentVariable("DB_PASSWORD");

		Properties connectionProps = new Properties();
		connectionProps.setProperty("user", user);
		connectionProps.setProperty("password", password);
		this.mysqlConnectionParameters = new MySqlConnectionParameters(jdbcString, connectionProps);
	}

	public DatabaseConnector(MySqlConnectionParameters mysqlConnectionParameters) {
		this.mysqlConnectionParameters = mysqlConnectionParameters;
	}

	public Connection getConnection() throws SQLException {
		System.err.println("DriverManager.getLoginTimeout(): " + DriverManager.getLoginTimeout());
		return DriverManager.getConnection(mysqlConnectionParameters.getJdbcUrl(), mysqlConnectionParameters.getConnectionProps());
	}

	public MySqlConnectionParameters getMysqlConnectionParameters() {
		return this.mysqlConnectionParameters;
	}
}
