package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.poorknight.house.commands.HouseCommand.OUTSIDE_LIGHTS_OFF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OutsideLightsOffRequestHandlerTest {

	@InjectMocks
	private OutsideLightsOffRequestHandler handler;

	@Mock
	private HouseCommandMessager messager;

	@Test
	public void delegatesCorrectly() {
		final EchoResponse response = handler.handle();

		verify(messager).requestHouseCommand(OUTSIDE_LIGHTS_OFF);
		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}