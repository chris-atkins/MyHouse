package com.poorknight.housestatus;

import com.poorknight.thermostat.ThermostatStatus;
import org.flywaydb.core.Flyway;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testcontainers.containers.MySQLContainer;

import java.sql.*;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(Enclosed.class)
public class HouseStatusRepositoryTest {

	@RunWith(JUnit4.class)
	public static class HouseStatusRepositoryIntegrationTest {
		private static Properties connectionProps;

		@ClassRule
		public static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.7.24")
				.withDatabaseName("myhouse")
				.withUsername("Chris")
				.withPassword("theBestPassword");

		@BeforeClass
		public static void setUp() throws Exception {
			connectionProps = new Properties();
			connectionProps.put("user", "Chris");
			connectionProps.put("password", "theBestPassword");

			Flyway flyway = Flyway.configure().dataSource(mySQLContainer.getJdbcUrl(), "Chris", "theBestPassword").load();
			flyway.migrate();
		}

		@After
		public void tearDown() throws Exception {
			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);

			Statement statement = connection.createStatement();
			statement.execute("DELETE FROM HOUSE_STATUS");
			statement.close();
			connection.close();
		}

		@Test
		public void jdbc_spiking() throws Exception {
			System.out.println(mySQLContainer.getJdbcUrl());

			Properties connectionProps = new Properties();
			connectionProps.put("user", "Chris");
			connectionProps.put("password", "theBestPassword");

			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			System.out.println("Connected to database");

			String insert1 = "INSERT INTO HOUSE_STATUS(TIME_UTC, TIME_LOCAL, HOUSE_TEMP) values (\"2017-01-01 00:00:00\", \"2018-01-01 00:00:00\", 68.5)";
			String insert2 = "INSERT INTO HOUSE_STATUS(TIME_UTC, TIME_LOCAL, HOUSE_TEMP) values (\"2017-02-02 00:00:00\", \"2018-02-02 00:00:00\", 64.25)";

			Statement statement = connection.createStatement();
			statement.execute(insert1);
			statement.execute(insert2);
			statement.close();

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");

			resultSet.next();
			int id = resultSet.getInt("ID");
			String utcTime = resultSet.getString("TIME_UTC");
			String localTime = resultSet.getString("TIME_LOCAL");
			double temp = resultSet.getDouble("HOUSE_TEMP");
			System.out.println("Row1: " + id + " | " + utcTime + " | " + localTime + " | " + temp);
			assertThat(id).isEqualTo(1);
			assertThat(utcTime).isEqualTo("2017-01-01 00:00:00");
			assertThat(localTime).isEqualTo("2018-01-01 00:00:00");
			assertThat(temp).isEqualTo(68.5);

			resultSet.next();
			id = resultSet.getInt("ID");
			utcTime = resultSet.getString("TIME_UTC");
			localTime = resultSet.getString("TIME_LOCAL");
			temp = resultSet.getDouble("HOUSE_TEMP");

			System.out.println("Row:2 " + id + " | " + utcTime + " | " + localTime + " | " + temp);
			assertThat(id).isEqualTo(2);
			assertThat(utcTime).isEqualTo("2017-02-02 00:00:00");
			assertThat(localTime).isEqualTo("2018-02-02 00:00:00");
			assertThat(temp).isEqualTo(64.25);

			statement.close();
			connection.close();
		}

		@Test
		public void inserts_correctly() throws Exception {
			Properties connectionProps = new Properties();
			connectionProps.put("user", "Chris");
			connectionProps.put("password", "theBestPassword");
			MySqlConnectionParameters mysqlConnectionParameters = new MySqlConnectionParameters(mySQLContainer.getJdbcUrl(), connectionProps);
			DatabaseConnector databaseConnector = new DatabaseConnector(mysqlConnectionParameters);
			HouseStatusRepository repository = new HouseStatusRepository(databaseConnector);

			DateTime utcTime = DateTime.parse("2018-03-03T12:30:00");
			DateTime localTime = DateTime.parse("2018-04-04T04:30:00");
			double houseTemp = 70.75;
			double tempSetting = 27.6;
			ThermostatStatus.FurnaceState furnaceState = ThermostatStatus.FurnaceState.HEAT_ON;
			HouseStatus houseStatus = new HouseStatus(utcTime, localTime, houseTemp, tempSetting, furnaceState.toString());

			repository.addHouseStatus(houseStatus);


			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");
			resultSet.next();

			int id = resultSet.getInt("ID");
			String utcTimeString = resultSet.getString("TIME_UTC");
			String localTimeString = resultSet.getString("TIME_LOCAL");
			double temp = resultSet.getDouble("HOUSE_TEMP");
			double setTemp = resultSet.getDouble("TEMP_SETTING");
			String stateOfFurnace = resultSet.getString("FURNACE_STATE");

			assertThat(utcTimeString).isEqualTo("2018-03-03 12:30:00");
			assertThat(localTimeString).isEqualTo("2018-04-04 04:30:00");
			assertThat(temp).isEqualTo(70.75);
			assertThat(setTemp).isEqualTo(27.6);
			assertThat(stateOfFurnace).isEqualTo("HEAT_ON");

			statement.close();
			connection.close();
		}
	}

	@RunWith(MockitoJUnitRunner.class)
	public static class HouseStatusRepositoryMockTest {

		@InjectMocks
		private HouseStatusRepository repository;

		@Mock
		private DatabaseConnector databaseConnector;

		@Mock
		private Connection connection;

		@Mock
		private Statement statement;

		private DateTime utcTime = DateTime.parse("2018-03-03T12:30:00");
		private DateTime localTime = DateTime.parse("2018-04-04T04:30:00");
		private double houseTemp = 70.75;
		private double tempSetting = 27.6;
		private String furnaceState = "HEAT_ON";
		private HouseStatus houseStatus = new HouseStatus(utcTime, localTime, houseTemp, tempSetting, furnaceState);

		@Test
		public void closesStatementAndConnectionOnExecutionError() throws Exception {
			when(databaseConnector.getConnection()).thenReturn(connection);
			when(connection.createStatement()).thenReturn(statement);

			SQLException sqlException = new SQLException();
			when(statement.execute(anyString())).thenThrow(sqlException);

			try {
				repository.addHouseStatus(houseStatus);
				fail("Expecting exception");
			} catch(RuntimeException e) {
				assertThat(e.getCause()).isEqualTo(sqlException);
			}

			verify(statement).close();
			verify(connection).close();
		}

		@Test
		public void closesConnectionEvenOnErrorClosingStatement() throws Exception {
			when(databaseConnector.getConnection()).thenReturn(connection);
			when(connection.createStatement()).thenReturn(statement);

			SQLException sqlException = new SQLException();
			doThrow(sqlException).when(statement).close();

			try {
				repository.addHouseStatus(houseStatus);
				fail("Expecting exception");
			} catch(RuntimeException e) {
				assertThat(e.getCause()).isEqualTo(sqlException);
			}

			verify(connection).close();
		}

		@Test
		public void wrapsExceptionWhenClosingConnectionThrows() throws Exception {
			when(databaseConnector.getConnection()).thenReturn(connection);
			when(connection.createStatement()).thenReturn(statement);

			SQLException sqlException = new SQLException();
			doThrow(sqlException).when(connection).close();

			try {
				repository.addHouseStatus(houseStatus);
				fail("Expecting exception");
			} catch(RuntimeException e) {
				assertThat(e.getCause()).isEqualTo(sqlException);
			}
		}
	}
}