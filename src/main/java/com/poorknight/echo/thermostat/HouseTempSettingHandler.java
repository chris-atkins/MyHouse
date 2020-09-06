package com.poorknight.echo.thermostat;

import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.thermostat.ThermostatMessager;
import com.poorknight.thermostat.ThermostatStatus;
import com.poorknight.thermostat.ThermostatStatus.ThermostatMode;

public class HouseTempSettingHandler implements EchoRequestHandler {

	private ThermostatMessager thermostatMessager;

	public HouseTempSettingHandler(ThermostatMessager thermostatMessager) {
		this.thermostatMessager = thermostatMessager;
	}

	@Override
	public EchoResponse handle() {
		ThermostatStatus thermostatStatus = thermostatMessager.requestThermostatStatus();
		return buildEchoResponse(thermostatStatus);
	}

	private EchoResponse buildEchoResponse(ThermostatStatus thermostatStatus) {
		double tempSetting = thermostatStatus.getTempSetting();
		String modeString = buildModeString(thermostatStatus.getThermostatMode());
		String onOffString = buildOnOffString(thermostatStatus.getFurnaceState());

		return EchoResponse.responseWithSpeech("The " + modeString + " is set to " + tempSetting + ", and is " + onOffString + ".");
	}

	private String buildModeString(ThermostatMode thermostatMode) {
		if (thermostatMode == ThermostatMode.AC_MODE) {
			return "AC";
		} else if (thermostatMode == ThermostatMode.FURNACE_MODE) {
			return "furnace";
		}
		throw new RuntimeException("HI");
	}

	private String buildOnOffString(ThermostatStatus.FurnaceState furnaceState) {
		if (furnaceState == ThermostatStatus.FurnaceState.OFF) {
			return "off";
		}
		return "on";
	}
}
