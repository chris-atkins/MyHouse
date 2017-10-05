package com.poorknight.echo.housecommand;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class HouseCommandTest {

	@Test
	public void atWork_hasTheCorrectString() throws Exception {
		assertThat(HouseCommand.AT_WORK_MODE.getCommandAsString()).isEqualTo("at-work-mode");
	}
}