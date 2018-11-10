package com.poorknight.persistence;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;


@RunWith(JUnit4.class)
//@Ignore
public class TemperatureRepositoryTest {


	public static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.7.24")
			.withDatabaseName("myhouse")
			.withUsername("Chris")
			.withPassword("theBestPassword");

	@BeforeClass
	public static void setUp() throws Exception {
		mySQLContainer.start();

		Thread.sleep(5000);

		Flyway flyway = Flyway.configure().dataSource(mySQLContainer.getJdbcUrl(), "Chris", "theBestPassword").load();
		flyway.migrate();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		mySQLContainer.stop();
		mySQLContainer.close();
	}

	@Test
	public void hello() throws Exception {
		System.out.println(mySQLContainer.getJdbcUrl());

		Properties connectionProps = new Properties();
		connectionProps.put("user", "Chris");
		connectionProps.put("password", "theBestPassword");

		Connection connection = DriverManager.getConnection(mySQLContainer.getJdbcUrl(), connectionProps);
		System.out.println("Connected to database");

		String insert = "INSERT INTO PERSON values (1, \"Chris Atkins\")";

		Statement statement = connection.createStatement();
		statement.execute(insert);
		statement.close();

		statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM PERSON");

		while(resultSet.next()) {
			int id = resultSet.getInt("ID");
			String name = resultSet.getString("NAME");
			System.out.println("Row: " + id + " | " + name);
			assertThat(id).isEqualTo(1);
			assertThat(name).isEqualTo("Chris Atkins");

		}
		statement.close();
		connection.close();
	}

}