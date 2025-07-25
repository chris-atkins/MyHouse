package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FancyLightOnRequestHandlerTest {

	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		FancyLightOnRequestHandler fancyLightOnRequestHandler = new FancyLightOnRequestHandler(houseCommandMessager);

		fancyLightOnRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.FANCY_LIGHT_ON);
	}

	@Test
	public void returnsCorrectResults() {
		FancyLightOnRequestHandler fancyLightOnRequestHandler = new FancyLightOnRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = fancyLightOnRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}