package com.poorknight.echo.thermostat;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.thermostat.ThermostatMessager;

import java.math.BigDecimal;

public class TempCheckHandler implements EchoRequestHandler {

	private final ThermostatMessager thermostatMessager;

	public TempCheckHandler(final ThermostatMessager thermostatMessager) {
		this.thermostatMessager = thermostatMessager;
	}

	@Override
	public EchoResponse handle() {
		final BigDecimal currentTemp = thermostatMessager.requestCurrentTemp();
		return EchoResponse.responseWithSpeech(buildResponseSpeech(currentTemp));
	}

	private String buildResponseSpeech(final BigDecimal currentTemp) {
		return "It's " + currentTemp.toString() + " degrees.";
	}
}
