package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BedroomOffRequestHandlerTest {


	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		BedroomOffRequestHandler bedroomOffRequestHandler = new BedroomOffRequestHandler(houseCommandMessager);

		bedroomOffRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.BEDROOM_OFF);
	}

	@Test
	public void returnsCorrectResults() {
		BedroomOffRequestHandler bedroomOffRequestHandler = new BedroomOffRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = bedroomOffRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}