package com.poorknight.echo.housecommand;

import com.poorknight.echo.EchoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_ON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OutsideLightsOnRequestHandlerTest {

	@InjectMocks
	private OutsideLightsOnRequestHandler handler;

	@Mock
	private HouseCommandMessager messager;

	@Test
	public void delegatesCorrectly() throws Exception {
		final EchoResponse response = handler.handle();

		verify(messager).requestHouseCommand(OUTSIDE_LIGHTS_ON);
		assertThat(response.getResponse().getOutputSpeech()).isNull();
	}
}