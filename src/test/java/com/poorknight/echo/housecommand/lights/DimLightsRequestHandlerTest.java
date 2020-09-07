package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class DimLightsRequestHandlerTest {


	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		DimLightsRequestHandler dimLightsRequestHandler = new DimLightsRequestHandler(houseCommandMessager);

		dimLightsRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.DIM_LIGHTS);
	}

	@Test
	public void returnsCorrectResults() {
		DimLightsRequestHandler dimLightsRequestHandler = new DimLightsRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = dimLightsRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}