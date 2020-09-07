package com.poorknight.echo.lights.off;

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
public class LightsOffRequestHandlerTest {

	@InjectMocks
	private LightsOffRequestHandler handler;

	@Mock
	private HouseCommandMessager houseCommandMessager;

	@Test
	public void sendsOffRequestWhenCalled() throws Exception {
		handler.handle();
		verify(houseCommandMessager).requestHouseCommand(HouseCommand.LIGHTS_OFF);
	}

	@Test
	public void returnsResponseWithNoText() {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech(), nullValue());
	}
}
