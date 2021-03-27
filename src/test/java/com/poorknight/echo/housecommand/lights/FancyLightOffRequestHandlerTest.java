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
public class FancyLightOffRequestHandlerTest {

	@Test
	public void delegatesCorrectly() {
		HouseCommandMessager houseCommandMessager = mock(HouseCommandMessager.class);
		FancyLightOffRequestHandler fancyLightOffRequestHandler = new FancyLightOffRequestHandler(houseCommandMessager);

		fancyLightOffRequestHandler.handle();

		verify(houseCommandMessager).requestHouseCommand(HouseCommand.FANCY_LIGHT_OFF);
	}

	@Test
	public void returnsCorrectResults() {
		FancyLightOffRequestHandler fancyLightOffRequestHandler = new FancyLightOffRequestHandler(mock(HouseCommandMessager.class));
		EchoResponse response = fancyLightOffRequestHandler.handle();

		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}