package com.poorknight.housestatus;

import com.poorknight.alerting.email.Emailer;
import com.poorknight.settings.Environment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Transport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DatabaseConnector.class, Environment.class })
public class DatabaseConnectorTest {

	@Test
	public void getsConnectionParamsFromEnvironmentVariablesWhenUsingEmptyConstructor() {
		PowerMockito.mockStatic(Transport.class);
		PowerMockito.mockStatic(Environment.class);
		when(Environment.getEnvironmentVariable("JDBC_CONNECTION_STRING")).thenReturn("oh hi");
		when(Environment.getEnvironmentVariable("DB_USER")).thenReturn("the best one");
		when(Environment.getEnvironmentVariable("DB_PASSWORD")).thenReturn("penultimate one out of two");

		DatabaseConnector databaseConnector = new DatabaseConnector();

		MySqlConnectionParameters mysqlConnectionParameters = databaseConnector.getMysqlConnectionParameters();
		assertThat(mysqlConnectionParameters.getJdbcUrl()).isEqualTo("oh hi");
		assertThat(mysqlConnectionParameters.getConnectionProps().getProperty("user")).isEqualTo("the best one");
		assertThat(mysqlConnectionParameters.getConnectionProps().getProperty("password")).isEqualTo("penultimate one out of two");
	}
}