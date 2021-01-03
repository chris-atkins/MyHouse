package com.poorknight.echo.housecommand.lights;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DiningLightsBrightRequestHandlerTest {

	@InjectMocks
	private DiningLightsBrightRequestHandler handler;

	@Mock
	private HouseCommandMessager houseMessager;

	@Test
	public void sendsOffRequestWhenCalled() {
		handler.handle();
		verify(houseMessager).requestHouseCommand(HouseCommand.DINING_LIGHTS_BRIGHT);
	}

	@Test
	public void returnsResponseWithNoText() {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech(), nullValue());
	}
}
