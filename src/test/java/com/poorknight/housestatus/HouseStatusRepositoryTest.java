package com.poorknight.housestatus;

import com.poorknight.housestatus.repository.DatabaseConnector;
import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.housestatus.repository.MySqlConnectionParameters;
import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.thermostat.ThermostatStatus;
import org.flywaydb.core.Flyway;
import org.joda.time.DateTime;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testcontainers.containers.MySQLContainer;

import java.sql.*;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(Enclosed.class)
public class HouseStatusRepositoryTest {

	@RunWith(JUnit4.class)
	@Ignore
	public static class HouseStatusRepositoryIntegrationTest {

		private static Properties connectionProps;
		private static HouseStatusRepository repository;


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

			MySqlConnectionParameters mysqlConnectionParameters = new MySqlConnectionParameters(mySQLContainer.getJdbcUrl(), connectionProps);
			DatabaseConnector databaseConnector = new DatabaseConnector(mysqlConnectionParameters);
			repository = new HouseStatusRepository(databaseConnector);
		}

		@After
		public void tearDown() throws Exception {
			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);

			Statement statement = connection.createStatement();
			statement.execute("DELETE FROM HOUSE_STATUS");
			statement.close();
			connection.close();
			System.out.println("Deleted All Records in tearDown()");
		}

		@Test
		public void jdbc_spiking() throws Exception {
			System.out.println(mySQLContainer.getJdbcUrl());

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
			int id1 = resultSet.getInt("ID");
			String utcTime = resultSet.getString("TIME_UTC");
			String localTime = resultSet.getString("TIME_LOCAL");
			double temp = resultSet.getDouble("HOUSE_TEMP");
			System.out.println("Row1: " + id1 + " | " + utcTime + " | " + localTime + " | " + temp);
			assertThat(id1).isGreaterThan(0);
			assertThat(utcTime).isEqualTo("2017-01-01 00:00:00");
			assertThat(localTime).isEqualTo("2018-01-01 00:00:00");
			assertThat(temp).isEqualTo(68.5);

			resultSet.next();
			int id2 = resultSet.getInt("ID");
			utcTime = resultSet.getString("TIME_UTC");
			localTime = resultSet.getString("TIME_LOCAL");
			temp = resultSet.getDouble("HOUSE_TEMP");

			System.out.println("Row:2 " + id2 + " | " + utcTime + " | " + localTime + " | " + temp);
			assertThat(id2).isEqualTo(id1 + 1);
			assertThat(utcTime).isEqualTo("2017-02-02 00:00:00");
			assertThat(localTime).isEqualTo("2018-02-02 00:00:00");
			assertThat(temp).isEqualTo(64.25);

			statement.close();
			connection.close();
		}

		@Test
		public void inserts_correctly() throws Exception {
			DateTime utcTime = DateTime.parse("2018-03-03T12:30:00");
			DateTime localTime = DateTime.parse("2018-04-04T04:30:00");
			double houseTemp = 70.75;
			double tempSetting = 27.6;
			ThermostatStatus.FurnaceState furnaceState = ThermostatStatus.FurnaceState.HEAT_ON;

			ThermostatStatus thermostatStatus = new ThermostatStatus(houseTemp, tempSetting, furnaceState);
			Double tempFahrenheit = 27.12;
			Double windSpeedMph = 9.17;
			Double humidityPercent = 81d;
			Double pressureHPa = 1017.12;
			WeatherStatus weatherStatus = new WeatherStatus(tempFahrenheit, windSpeedMph, humidityPercent, pressureHPa);

			repository.addHouseStatus(utcTime, localTime, thermostatStatus, weatherStatus);

			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");
			resultSet.next();

			String utcTimeString = resultSet.getString("TIME_UTC");
			String localTimeString = resultSet.getString("TIME_LOCAL");
			double temp = resultSet.getDouble("HOUSE_TEMP");
			double setTemp = resultSet.getDouble("TEMP_SETTING");
			String stateOfFurnace = resultSet.getString("FURNACE_STATE");
			double outsideTemp = resultSet.getDouble("EXTERNAL_TEMP_F");
			double windSpeed = resultSet.getDouble("EXTERNAL_WIND_SPEED_MPH");
			double humidity = resultSet.getDouble("EXTERNAL_HUMIDITY_PERCENT");
			double pressure = resultSet.getDouble("EXTERNAL_PRESSURE_HPA");

			assertThat(utcTimeString).isEqualTo("2018-03-03 12:30:00");
			assertThat(localTimeString).isEqualTo("2018-04-04 04:30:00");
			assertThat(temp).isEqualTo(70.75);
			assertThat(setTemp).isEqualTo(27.6);
			assertThat(stateOfFurnace).isEqualTo("HEAT_ON");
			assertThat(outsideTemp).isEqualTo(27.12);
			assertThat(windSpeed).isEqualTo(9.17);
			assertThat(humidity).isEqualTo(81d);
			assertThat(pressure).isEqualTo(1017.12);

			statement.close();
			connection.close();
		}


		@Test
		public void reportsCorrectly() {
			WeatherStatus weatherStatus = new WeatherStatus(27.12, 9.17, 81d, 1017.12);


			DateTime utcTime1 = DateTime.parse("2018-03-03T12:30:00");
			DateTime localTime1 = DateTime.parse("2018-04-04T04:30:00");
			double houseTemp1 = 70.75;
			double tempSetting1 = 27.6;
			ThermostatStatus thermostatStatus1 = new ThermostatStatus(houseTemp1, tempSetting1, ThermostatStatus.FurnaceState.HEAT_ON);


			DateTime utcTime2 = DateTime.parse("2018-03-03T12:31:00");
			DateTime localTime2 = DateTime.parse("2018-04-04T04:31:00");
			double houseTemp2 = 70.75;
			double tempSetting2 = 27.6;
			ThermostatStatus thermostatStatus2 = new ThermostatStatus(houseTemp2, tempSetting2, ThermostatStatus.FurnaceState.HEAT_ON);


			DateTime utcTime3 = DateTime.parse("2018-03-03T12:29:00");
			DateTime localTime3 = DateTime.parse("2018-04-04T04:29:00");
			ThermostatStatus thermostatStatus3 = new ThermostatStatus(-1, -1, ThermostatStatus.FurnaceState.HEAT_ON);

			DateTime utcTime4 = DateTime.parse("2018-03-03T12:32:00");
			DateTime localTime4 = DateTime.parse("2018-04-04T04:32:00");
			ThermostatStatus thermostatStatus4 = new ThermostatStatus(-1, -1, ThermostatStatus.FurnaceState.HEAT_ON);


			repository.addHouseStatus(utcTime1, localTime1, thermostatStatus1, weatherStatus);
			repository.addHouseStatus(utcTime2, localTime2, thermostatStatus2, weatherStatus);

			repository.addHouseStatus(utcTime3, localTime3, thermostatStatus3, weatherStatus);
			repository.addHouseStatus(utcTime4, localTime4, thermostatStatus4, weatherStatus);


			List<HouseDataPoint> houseDataPoints = repository.retrieveHouseStatusFrom(DateTime.parse("2018-03-03T12:30:00"), DateTime.parse("2018-03-03T12:31:00"));

			HouseDataPoint dataPoint1 = new HouseDataPoint(localTime1, houseTemp1, tempSetting1);
			HouseDataPoint dataPoint2 = new HouseDataPoint(localTime2, houseTemp2, tempSetting2);

			assertThat(houseDataPoints).containsExactly(dataPoint1, dataPoint2);
		}

		@Test
		public void handlesTempWith2DecimalPlacesOver100() throws Exception {
			ThermostatStatus thermostatStatus = new ThermostatStatus(1d, 1d, ThermostatStatus.FurnaceState.OFF);

			Double tempFahrenheit = 101.01;
			WeatherStatus weatherStatus = new WeatherStatus(tempFahrenheit, 1d, 1d, 1d);

			repository.addHouseStatus(new DateTime(), new DateTime(), thermostatStatus, weatherStatus);


			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");
			resultSet.next();

			double outsideTemp = resultSet.getDouble("EXTERNAL_TEMP_F");

			assertThat(outsideTemp).isEqualTo(101.01);

			statement.close();
			connection.close();
		}

		@Test
		public void handlesNegativeTempWith2DecimalPlaces() throws Exception {
			ThermostatStatus thermostatStatus = new ThermostatStatus(1d, 1d, ThermostatStatus.FurnaceState.OFF);

			Double tempFahrenheit = -13.55;
			WeatherStatus weatherStatus = new WeatherStatus(tempFahrenheit, 1d, 1d, 1d);

			repository.addHouseStatus(new DateTime(), new DateTime(), thermostatStatus, weatherStatus);


			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");
			resultSet.next();

			double outsideTemp = resultSet.getDouble("EXTERNAL_TEMP_F");

			assertThat(outsideTemp).isEqualTo(-13.55);

			statement.close();
			connection.close();
		}

		@Test
		public void handles100PercentHumidity() throws Exception {
			ThermostatStatus thermostatStatus = new ThermostatStatus(1d, 1d, ThermostatStatus.FurnaceState.OFF);

			Double humidity = 100d;
			WeatherStatus weatherStatus = new WeatherStatus(1d, 1d, humidity, 1d);

			repository.addHouseStatus(new DateTime(), new DateTime(), thermostatStatus, weatherStatus);


			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");
			resultSet.next();

			double outsideTemp = resultSet.getDouble("EXTERNAL_HUMIDITY_PERCENT");

			assertThat(outsideTemp).isEqualTo(100d);

			statement.close();
			connection.close();
		}

		@Test
		public void handlesTwoDecimalPlaceHumidity() throws Exception {
			ThermostatStatus thermostatStatus = new ThermostatStatus(1d, 1d, ThermostatStatus.FurnaceState.OFF);

			Double humidity = 55.55;
			WeatherStatus weatherStatus = new WeatherStatus(1d, 1d, humidity, 1d);

			repository.addHouseStatus(new DateTime(), new DateTime(), thermostatStatus, weatherStatus);


			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");
			resultSet.next();

			double humidityPercent = resultSet.getDouble("EXTERNAL_HUMIDITY_PERCENT");

			assertThat(humidityPercent).isEqualTo(55.55);

			statement.close();
			connection.close();
		}

		@Test
		public void handlesHurricaneForceWinds() throws Exception {
			ThermostatStatus thermostatStatus = new ThermostatStatus(1d, 1d, ThermostatStatus.FurnaceState.OFF);

			Double wind = 155.55;
			WeatherStatus weatherStatus = new WeatherStatus(1d, wind, 1d, 1d);

			repository.addHouseStatus(new DateTime(), new DateTime(), thermostatStatus, weatherStatus);


			Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOUSE_STATUS");
			resultSet.next();

			double windSpeed = resultSet.getDouble("EXTERNAL_WIND_SPEED_MPH");

			assertThat(windSpeed).isEqualTo(155.55);

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
		private ThermostatStatus.FurnaceState furnaceState = ThermostatStatus.FurnaceState.HEAT_ON;
		private ThermostatStatus thermostatStatus = new ThermostatStatus(houseTemp, tempSetting, furnaceState);
		private Double tempFahrenheit = 27.12;
		private Double windSpeedMph = 9.17;
		private Double humidityPercent = 81d;
		private Double pressureHPa = 1017d;
		private WeatherStatus weatherStatus = new WeatherStatus(tempFahrenheit, windSpeedMph, humidityPercent, pressureHPa);

		@Test
		public void closesStatementAndConnectionOnExecutionError() throws Exception {
			when(databaseConnector.getConnection()).thenReturn(connection);
			when(connection.createStatement()).thenReturn(statement);

			SQLException sqlException = new SQLException();
			when(statement.execute(anyString())).thenThrow(sqlException);

			try {
				repository.addHouseStatus(utcTime, localTime, thermostatStatus, weatherStatus);
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
				repository.addHouseStatus(utcTime, localTime, thermostatStatus, weatherStatus);
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
				repository.addHouseStatus(utcTime, localTime, thermostatStatus, weatherStatus);
				fail("Expecting exception");
			} catch(RuntimeException e) {
				assertThat(e.getCause()).isEqualTo(sqlException);
			}
		}

		@Test
		public void queryClosesStatementAndConnectionOnExecutionError() throws Exception {
			when(databaseConnector.getConnection()).thenReturn(connection);
			when(connection.createStatement()).thenReturn(statement);

			SQLException sqlException = new SQLException();
			when(statement.executeQuery(anyString())).thenThrow(sqlException);

			try {
				repository.retrieveHouseStatusFrom(utcTime, localTime);
				fail("Expecting exception");
			} catch(RuntimeException e) {
				assertThat(e.getCause()).isEqualTo(sqlException);
			}

			verify(statement).close();
			verify(connection).close();
		}

		@Test
		public void queryClosesConnectionEvenOnErrorClosingStatement() throws Exception {
			when(databaseConnector.getConnection()).thenReturn(connection);
			when(connection.createStatement()).thenReturn(statement);
			ResultSet resultSet = Mockito.mock(ResultSet.class);
			when(resultSet.next()).thenReturn(false);
			when(statement.executeQuery(anyString())).thenReturn(resultSet);

			SQLException sqlException = new SQLException();
			doThrow(sqlException).when(statement).close();

			try {
				repository.retrieveHouseStatusFrom(utcTime, localTime);
				fail("Expecting exception");
			} catch(RuntimeException e) {
				assertThat(e.getCause()).isEqualTo(sqlException);
			}

			verify(connection).close();
		}

		@Test
		public void queryWrapsExceptionWhenClosingConnectionThrows() throws Exception {
			when(databaseConnector.getConnection()).thenReturn(connection);
			when(connection.createStatement()).thenReturn(statement);
			ResultSet resultSet = Mockito.mock(ResultSet.class);
			when(resultSet.next()).thenReturn(false);
			when(statement.executeQuery(anyString())).thenReturn(resultSet);

			SQLException sqlException = new SQLException();
			doThrow(sqlException).when(connection).close();

			try {
				repository.addHouseStatus(utcTime, localTime, thermostatStatus, weatherStatus);
				fail("Expecting exception");
			} catch(RuntimeException e) {
				assertThat(e.getCause()).isEqualTo(sqlException);
			}
		}
	}
}