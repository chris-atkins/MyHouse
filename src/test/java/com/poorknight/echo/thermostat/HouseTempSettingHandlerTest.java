package com.poorknight.echo.thermostat;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.thermostat.ThermostatMessager;
import com.poorknight.house.thermostat.ThermostatStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HouseTempSettingHandlerTest {

	@InjectMocks
	HouseTempSettingHandler houseTempSettingHandler;

	@Mock
	ThermostatMessager thermostatMessager;

	@Test
	public void returnsResponseBasedOnDataFromHouse_FurnaceMode() {
		ThermostatStatus thermostatStatus = new ThermostatStatus(12.4, 45.8, ThermostatStatus.FurnaceState.HEAT_ON, ThermostatStatus.ThermostatMode.FURNACE_MODE);
		when(thermostatMessager.requestThermostatStatus()).thenReturn(thermostatStatus);

		EchoResponse response = houseTempSettingHandler.handle();

		assertThat(response.getResponse().getOutputSpeech().getText()).isEqualTo("The furnace is set to 45.8, and is on.");
		assertThat(response.getResponse().getShouldEndSession()).isTrue();
	}

	@Test
	public void returnsResponseBasedOnDataFromHouse_ACMode() {
		ThermostatStatus thermostatStatus = new ThermostatStatus(12.4, 45.7, ThermostatStatus.FurnaceState.AC_ON, ThermostatStatus.ThermostatMode.AC_MODE);
		when(thermostatMessager.requestThermostatStatus()).thenReturn(thermostatStatus);

		EchoResponse response = houseTempSettingHandler.handle();

		assertThat(response.getResponse().getOutputSpeech().getText()).isEqualTo("The AC is set to 45.7, and is on.");
		assertThat(response.getResponse().getShouldEndSession()).isTrue();
	}

	@Test
	public void returnsResponseBasedOnDataFromHouse_ACMode_FanOff() {
		ThermostatStatus thermostatStatus = new ThermostatStatus(12.4, 48.7, ThermostatStatus.FurnaceState.OFF, ThermostatStatus.ThermostatMode.AC_MODE);
		when(thermostatMessager.requestThermostatStatus()).thenReturn(thermostatStatus);

		EchoResponse response = houseTempSettingHandler.handle();

		assertThat(response.getResponse().getOutputSpeech().getText()).isEqualTo("The AC is set to 48.7, and is off.");
		assertThat(response.getResponse().getShouldEndSession()).isTrue();
	}
}