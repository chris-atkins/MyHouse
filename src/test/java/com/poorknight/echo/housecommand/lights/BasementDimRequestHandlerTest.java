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
public class BasementDimRequestHandlerTest {


	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		BasementDimRequestHandler basementDimRequestHandler = new BasementDimRequestHandler(houseCommandMessager);

		basementDimRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.BASEMENT_DIM);
	}

	@Test
	public void returnsCorrectResults() {
		BasementDimRequestHandler basementDimRequestHandler = new BasementDimRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = basementDimRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}