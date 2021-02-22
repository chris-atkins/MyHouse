package com.poorknight.house.commands;

import com.poorknight.house.commands.HouseCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class HouseCommandTest {

	@Test
	public void atWorkMode_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.AT_WORK_MODE.asPiString()).isEqualTo("at-work-mode");
	}

	@Test
	public void lightsOn_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.LIGHTS_ON.asPiString()).isEqualTo("lights-on");
	}

	@Test
	public void lightsOff_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.LIGHTS_OFF.asPiString()).isEqualTo("lights-off");
	}

	@Test
	public void outsideLightsOff_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.OUTSIDE_LIGHTS_OFF.asPiString()).isEqualTo("outside-lights-off");
	}

	@Test
	public void outdsideLightsOn_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.OUTSIDE_LIGHTS_ON.asPiString()).isEqualTo("outside-lights-on");
	}

	@Test
	public void dimLights_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.DIM_LIGHTS.asPiString()).isEqualTo("dim-lights");
	}

	@Test
	public void houseTempUp_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.HOUSE_TEMP_UP.asPiString()).isEqualTo("house-temp-up");
	}

	@Test
	public void houseTempDown_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.HOUSE_TEMP_DOWN.asPiString()).isEqualTo("house-temp-down");
	}


	@Test
	public void basementOff_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.BASEMENT_OFF.asPiString()).isEqualTo("basement-off");
	}


	@Test
	public void basementDim_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.BASEMENT_DIM.asPiString()).isEqualTo("basement-dim");
	}


	@Test
	public void basementOn_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.BASEMENT_ON.asPiString()).isEqualTo("basement-on");
	}


	@Test
	public void diningLightBright_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.DINING_LIGHTS_BRIGHT.asPiString()).isEqualTo("dining-lights-bright");
	}


	@Test
	public void bedroomOn_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.BEDROOM_ON.asPiString()).isEqualTo("bedroom-on");
	}


	@Test
	public void bedroomOff_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.BEDROOM_OFF.asPiString()).isEqualTo("bedroom-off");
	}

	@Test
	public void canBuildHouseCommandFromString() {
		assertThat(HouseCommand.fromPiString("at-work-mode")).isEqualTo(HouseCommand.AT_WORK_MODE);
		assertThat(HouseCommand.fromPiString("lights-on")).isEqualTo(HouseCommand.LIGHTS_ON);
		assertThat(HouseCommand.fromPiString("lights-off")).isEqualTo(HouseCommand.LIGHTS_OFF);
		assertThat(HouseCommand.fromPiString("outside-lights-on")).isEqualTo(HouseCommand.OUTSIDE_LIGHTS_ON);
		assertThat(HouseCommand.fromPiString("outside-lights-off")).isEqualTo(HouseCommand.OUTSIDE_LIGHTS_OFF);
		assertThat(HouseCommand.fromPiString("dim-lights")).isEqualTo(HouseCommand.DIM_LIGHTS);
		assertThat(HouseCommand.fromPiString("house-temp-up")).isEqualTo(HouseCommand.HOUSE_TEMP_UP);
		assertThat(HouseCommand.fromPiString("house-temp-down")).isEqualTo(HouseCommand.HOUSE_TEMP_DOWN);
		assertThat(HouseCommand.fromPiString("basement-off")).isEqualTo(HouseCommand.BASEMENT_OFF);
		assertThat(HouseCommand.fromPiString("basement-dim")).isEqualTo(HouseCommand.BASEMENT_DIM);
		assertThat(HouseCommand.fromPiString("basement-on")).isEqualTo(HouseCommand.BASEMENT_ON);
		assertThat(HouseCommand.fromPiString("dining-lights-bright")).isEqualTo(HouseCommand.DINING_LIGHTS_BRIGHT);
		assertThat(HouseCommand.fromPiString("bedroom-on")).isEqualTo(HouseCommand.BEDROOM_ON);
		assertThat(HouseCommand.fromPiString("bedroom-off")).isEqualTo(HouseCommand.BEDROOM_OFF);
	}

	@Test
	public void throwsException_ForUnrecognizedResponseString() {
		try {
			HouseCommand.fromPiString("hi there");
			fail("expecting exception");
		} catch (RuntimeException e) {
			assertThat(e.getMessage()).isEqualTo("Cannot translate string into HouseCommand. The passed string does not represent a valid HouseCommand: hi there");
		}
	}
}