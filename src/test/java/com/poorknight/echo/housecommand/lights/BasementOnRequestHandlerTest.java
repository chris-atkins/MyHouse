package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class BasementOnRequestHandlerTest {


	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		BasementOnRequestHandler basementOnRequestHandler = new BasementOnRequestHandler(houseCommandMessager);

		basementOnRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.BASEMENT_ON);
	}

	@Test
	public void returnsCorrectResults() {
		BasementOnRequestHandler basementOnRequestHandler = new BasementOnRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = basementOnRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}