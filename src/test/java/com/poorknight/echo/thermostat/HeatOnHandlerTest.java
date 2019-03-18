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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HeatOnHandlerTest {

	@InjectMocks
	private HeatOnHandler handler;

	@Mock
	private ThermostatMessager thermostatMessager;

	@Test
	public void tellsTheThermostatToSetTheHeat_2DegreesHigherThanCurrentTemperature() throws Exception {
		when(thermostatMessager.requestCurrentTemp()).thenReturn(new BigDecimal("68.5"));

		final EchoResponse response = handler.handle();

		verify(thermostatMessager).postHeatTargetTemperature(new BigDecimal("70.5"));
		assertThat(response.getResponse().getOutputSpeech().getText()).isEqualTo("Heat set to 70.5.");
		assertThat(response.getResponse().getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getResponse().getShouldEndSession()).isEqualTo(true);
	}

	@Test
	public void ifTheCurrentTempIs72OrGreater_NoRequestIsMadeToChangeTheTemp() throws Exception {
		when(thermostatMessager.requestCurrentTemp()).thenReturn(new BigDecimal("72"));

		final EchoResponse response = handler.handle();

		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));

		assertThat(response.getResponse().getOutputSpeech().getText()).isEqualTo("Don't you think it's hot enough already?");
		assertThat(response.getResponse().getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getResponse().getShouldEndSession()).isEqualTo(true);

		when(thermostatMessager.requestCurrentTemp()).thenReturn(new BigDecimal("73.5"));
		handler.handle();
		verify(thermostatMessager, never()).postHeatTargetTemperature(any(BigDecimal.class));
	}

	@Test
	public void ifTheNormalTempIncrementWillResultInHigherThan72_TheTempWillBeSetTo72() throws Exception {
		when(thermostatMessager.requestCurrentTemp()).thenReturn(new BigDecimal("71"));

		final EchoResponse response = handler.handle();

		verify(thermostatMessager).postHeatTargetTemperature(new BigDecimal("72"));
		assertThat(response.getResponse().getOutputSpeech().getText()).isEqualTo("Heat set to 72.");
	}
}