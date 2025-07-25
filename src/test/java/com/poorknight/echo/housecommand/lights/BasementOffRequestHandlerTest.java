package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BasementOffRequestHandlerTest {


	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		BasementOffRequestHandler basementOffRequestHandler = new BasementOffRequestHandler(houseCommandMessager);

		basementOffRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.BASEMENT_OFF);
	}

	@Test
	public void returnsCorrectResults() {
		BasementOffRequestHandler basementOffRequestHandler = new BasementOffRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = basementOffRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}