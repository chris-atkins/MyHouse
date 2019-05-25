package com.poorknight.echo.housecommand;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.thermostat.ThermostatMessager;

import java.math.BigDecimal;

public class HouseTempUpHandler implements EchoRequestHandler {

	private static final BigDecimal TEMP_INCREMENT = new BigDecimal("2");
	private static final BigDecimal MAX_ALLOWED_TEMP = new BigDecimal("72");

	private final ThermostatMessager thermostatMessager;

	public HouseTempUpHandler(final ThermostatMessager thermostatMessager) {
		this.thermostatMessager = thermostatMessager;
	}

	@Override
	public EchoResponse handle() {
		final BigDecimal currentTemp = thermostatMessager.requestCurrentTemp();
		if(tempValueIsHigherThanMaxAllowed(currentTemp)) {
			return EchoResponse.responseWithSpeech("Don't you think it's hot enough already?");
		}

		BigDecimal newTargetTemp = currentTemp.add(TEMP_INCREMENT);
		if(tempValueIsHigherThanMaxAllowed(newTargetTemp)) {
			newTargetTemp = MAX_ALLOWED_TEMP;
		}

		thermostatMessager.postHeatTargetTemperature(newTargetTemp);
		return EchoResponse.responseWithSpeech(buildResponseText(newTargetTemp));
	}

	private boolean tempValueIsHigherThanMaxAllowed(final BigDecimal currentTemp) {
		return currentTemp.compareTo(MAX_ALLOWED_TEMP) >= 0;
	}

	private String buildResponseText(final BigDecimal newTargetTemp) {
		return "Heat set to " + newTargetTemp.toString() + ".";
	}
}
