package com.poorknight.echo.thermostat;

import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.EchoResponseData;
import com.poorknight.house.thermostat.ThermostatMessager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TempCheckHandlerTest {

	@InjectMocks
	private TempCheckHandler handler;

	@Mock
	private ThermostatMessager thermostatMessager;

	@Test
	public void whenARequestIsHandled_TheThermostatMessagerIsCalled_AndItsResponseIsReturned() throws Exception {
		when(thermostatMessager.requestCurrentTemp()).thenReturn(new BigDecimal("42.5"));
		final EchoResponse echoResponse = handler.handle();

		final EchoResponseData response = echoResponse.getResponse();
		assertThat(response.getOutputSpeech().getText()).isEqualTo("It's 42.5 degrees.");
		assertThat(response.getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getShouldEndSession()).isTrue();
	}

	@Test
	public void whenRequestIsHandled_TrailingZeroDecimalPlaces_AreTruncated() throws Exception {
		when(thermostatMessager.requestCurrentTemp()).thenReturn(new BigDecimal("42.0"));
		final EchoResponse echoResponse = handler.handle();

		final EchoResponseData response = echoResponse.getResponse();
		assertThat(response.getOutputSpeech().getText()).isEqualTo("It's 42 degrees.");
		assertThat(response.getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getShouldEndSession()).isTrue();
	}
}