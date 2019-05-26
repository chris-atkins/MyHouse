package com.poorknight.echo.housecommand.temperature;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class HouseTemperatureModeTest {

	@Test
	public void hasCorrectStringForAC() {
		assertThat(HouseTemperatureMode.AC.asPiString()).isEqualTo("AC");
	}

	@Test
	public void returnsCorrectEnumForString_AC() {
		assertThat(HouseTemperatureMode.fromPiString("AC")).isEqualTo(HouseTemperatureMode.AC);
	}

	@Test
	public void hasCorrectStringForFurnace() {
		assertThat(HouseTemperatureMode.FURNACE.asPiString()).isEqualTo("furnace");
	}

	@Test
	public void returnsCorrectEnumForString_furnace() {
		assertThat(HouseTemperatureMode.fromPiString("furnace")).isEqualTo(HouseTemperatureMode.FURNACE);
	}

	@Test
	public void throwsException_ForUnrecognizedResponseString() {
		try {
			HouseTemperatureMode.fromPiString("hi there");
			fail("expecting exception");
		} catch (RuntimeException e) {
			assertThat(e.getMessage()).isEqualTo("Cannot translate string into HouseTemperatureMode. The passed string does not represent a valid HouseTemperatureMode: hi there");
		}
	}
}