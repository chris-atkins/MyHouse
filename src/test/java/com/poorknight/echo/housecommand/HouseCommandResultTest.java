package com.poorknight.echo.housecommand;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class HouseCommandResultTest {

	@Test
	public void hasCorrectStringForSuccess() {
		assertThat(HouseCommandResult.SUCCESS.asPiString()).isEqualTo("success");
	}

	@Test
	public void returnsCorrectEnumForString_success() {
		assertThat(HouseCommandResult.fromPiString("success")).isEqualTo(HouseCommandResult.SUCCESS);
	}

	@Test
	public void hasCorrectStringForFailure() {
		assertThat(HouseCommandResult.FAILURE.asPiString()).isEqualTo("failure");
	}

	@Test
	public void returnsCorrectEnumForString_failure() {
		assertThat(HouseCommandResult.fromPiString("failure")).isEqualTo(HouseCommandResult.FAILURE);
	}

	@Test
	public void hasCorrectStringForNoChange() {
		assertThat(HouseCommandResult.NO_CHANGE.asPiString()).isEqualTo("no-change");
	}

	@Test
	public void returnsCorrectEnumForString_nochange() {
		assertThat(HouseCommandResult.fromPiString("no-change")).isEqualTo(HouseCommandResult.NO_CHANGE);
	}

	@Test
	public void throwsException_ForUnrecognizedResponseString() {
		try {
			HouseCommandResult.fromPiString("hi there");
			fail("expecting exception");
		} catch (RuntimeException e) {
			assertThat(e.getMessage()).isEqualTo("Cannot translate string into HouseCommandResult. The passed string does not represent a valid HouseCommandResult: hi there");
		}
	}
}