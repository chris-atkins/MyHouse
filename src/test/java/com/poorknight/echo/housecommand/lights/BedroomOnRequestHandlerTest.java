package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BedroomOnRequestHandlerTest {


	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		BedroomOnRequestHandler bedroomOnRequestHandler = new BedroomOnRequestHandler(houseCommandMessager);

		bedroomOnRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.BEDROOM_ON);
	}

	@Test
	public void returnsCorrectResults() {
		BedroomOnRequestHandler bedroomOnRequestHandler = new BedroomOnRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = bedroomOnRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}