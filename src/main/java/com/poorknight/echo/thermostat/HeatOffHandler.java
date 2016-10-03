package com.poorknight.echo.thermostat;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.thermostat.ThermostatMessager;

import java.math.BigDecimal;

public class HeatOffHandler implements EchoRequestHandler {

	private static final BigDecimal TEMP_INCREMENT = new BigDecimal("2");

	private final ThermostatMessager thermostatMessager;

	public HeatOffHandler(final ThermostatMessager thermostatMessager) {
		this.thermostatMessager = thermostatMessager;
	}

	@Override
	public EchoResponse handle() {
		final BigDecimal currentTemp = thermostatMessager.requestCurrentTemp();
		final BigDecimal newTargetTemp = currentTemp.subtract(TEMP_INCREMENT);
		thermostatMessager.postHeatTargetTemperature(newTargetTemp);

		return EchoResponse.responseWithSpeech(buildResponseText(newTargetTemp));
	}

	private String buildResponseText(final BigDecimal newTargetTemp) {
		return "Heat turned down to " + newTargetTemp.toString() + ".";
	}
}
