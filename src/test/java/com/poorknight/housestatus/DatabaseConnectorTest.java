package com.poorknight.housestatus;

import com.poorknight.housestatus.repository.DatabaseConnector;
import com.poorknight.housestatus.repository.DatabaseConnectionParameters;
import com.poorknight.server.settings.Environment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DatabaseConnectorTest {

    @Test
    public void getsConnectionParamsFromEnvironmentVariablesWhenUsingEmptyConstructor() {
        try (MockedStatic<Environment> mockedEnvironment = Mockito.mockStatic(Environment.class)) {

            mockedEnvironment.when(() -> Environment.getEnvironmentVariable("JDBC_CONNECTION_STRING")).thenReturn("oh hi");
            mockedEnvironment.when(() -> Environment.getEnvironmentVariable("DB_USER")).thenReturn("the best one");
            mockedEnvironment.when(() -> Environment.getEnvironmentVariable("DB_PASSWORD")).thenReturn("penultimate one out of two");

            DatabaseConnector databaseConnector = new DatabaseConnector();

            DatabaseConnectionParameters mysqlConnectionParameters = databaseConnector.getDatabaseConnectionParameters();
            assertThat(mysqlConnectionParameters.getJdbcUrl()).isEqualTo("oh hi");
            assertThat(mysqlConnectionParameters.getConnectionProps().getProperty("user")).isEqualTo("the best one");
            assertThat(mysqlConnectionParameters.getConnectionProps().getProperty("password")).isEqualTo("penultimate one out of two");
        }
    }
}