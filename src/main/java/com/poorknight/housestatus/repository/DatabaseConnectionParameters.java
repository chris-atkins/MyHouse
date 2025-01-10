package com.poorknight.housestatus.repository;

import java.util.Properties;

public class DatabaseConnectionParameters {

	private final String jdbcUrl;
	private final Properties connectionProps;

	public DatabaseConnectionParameters(String jdbcUrl, Properties connectionProps) {
		this.jdbcUrl = jdbcUrl;
		this.connectionProps = connectionProps;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public Properties getConnectionProps() {
		return connectionProps;
	}
}
