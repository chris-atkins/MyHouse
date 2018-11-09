package com.poorknight.persistence;

import org.flywaydb.core.Flyway;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testcontainers.containers.MySQLContainer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
//@Ignore
public class TemperatureRepositoryAgainTest {

	@Test
	public void name() {
		String path = System.getenv("PATH");
		assertThat(path).isNotNull();
		System.out.println("path: " + path);

		String pathSeparator = File.pathSeparator;
		assertThat(pathSeparator).isNotNull();
		System.out.println("pathSeparator: " + pathSeparator);


		String quote = Pattern.quote(pathSeparator);
		assertThat(path).isNotNull();
		System.out.println("quote: " + quote);


		String[] split = path.split(quote);
		assertThat(split).isNotNull();
		System.out.println("split: " + split);
		for (String s : split) {
			System.out.println("pathPiece: " + s);
		}
	}
}