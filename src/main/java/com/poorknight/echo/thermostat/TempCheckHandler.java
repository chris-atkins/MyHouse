package com.poorknight.echo.thermostat;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.thermostat.ThermostatMessager;

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
		String tempString = buildRoundedTempString(currentTemp);
		return "It's " + tempString + " degrees.";
	}

	private String buildRoundedTempString(BigDecimal currentTemp) {
		String tempString = currentTemp.toString();
		if (tempString.endsWith(".0")) {
			tempString = tempString.substring(0, tempString.length() - 2);
		}
		return tempString;
	}
}
