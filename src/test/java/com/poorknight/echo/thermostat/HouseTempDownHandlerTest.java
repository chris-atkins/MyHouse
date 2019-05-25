package com.poorknight.echo.thermostat;

import com.poorknight.echo.EchoResponse;
import com.poorknight.thermostat.ThermostatMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HouseTempDownHandlerTest {

	@InjectMocks
	private HouseTempDownHandler handler;

	@Mock
	private ThermostatMessager thermostatMessager;

	@Test
	public void tellsTheThermostatToSetTheHeat_2DegreesLowerThanCurrentTemperature() throws Exception {
		when(thermostatMessager.requestCurrentTemp()).thenReturn(new BigDecimal("70.5"));

		final EchoResponse response = handler.handle();

		verify(thermostatMessager).postHeatTargetTemperature(new BigDecimal("68.5"));
		assertThat(response.getResponse().getOutputSpeech().getText()).isEqualTo("Heat turned down to 68.5.");
		assertThat(response.getResponse().getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getResponse().getShouldEndSession()).isEqualTo(true);
	}
}