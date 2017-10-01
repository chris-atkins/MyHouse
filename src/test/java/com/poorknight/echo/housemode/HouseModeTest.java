package com.poorknight.echo.housemode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class HouseModeTest {

	@Test
	public void atWork_hasTheCorrectString() throws Exception {
		assertThat(HouseMode.AT_WORK.getModeAsString()).isEqualTo("at-work");
	}
}