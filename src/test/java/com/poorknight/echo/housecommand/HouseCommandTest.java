package com.poorknight.echo.housecommand;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class HouseCommandTest {

	@Test
	public void atWorkMode_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.AT_WORK_MODE.getCommandAsString()).isEqualTo("at-work-mode");
	}

	@Test
	public void lightsOn_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.LIGHTS_ON.getCommandAsString()).isEqualTo("lights-on");
	}

	@Test
	public void lightsOff_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.LIGHTS_OFF.getCommandAsString()).isEqualTo("lights-off");
	}

	@Test
	public void outsideLightsOff_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.OUTSIDE_LIGHTS_OFF.getCommandAsString()).isEqualTo("outside-lights-off");
	}

	@Test
	public void outdsideLightsOn_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.OUTSIDE_LIGHTS_ON.getCommandAsString()).isEqualTo("outside-lights-on");
	}

	@Test
	public void dimLights_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.DIM_LIGHTS.getCommandAsString()).isEqualTo("dim-lights");
	}
}