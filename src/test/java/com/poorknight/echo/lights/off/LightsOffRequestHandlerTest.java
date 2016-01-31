package com.poorknight.echo.lights.off;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.lights.off.LightsOffRequestHandler;
import com.poorknight.lights.HueMessager;

@RunWith(MockitoJUnitRunner.class)
public class LightsOffRequestHandlerTest {

	@InjectMocks
	private LightsOffRequestHandler handler;

	@Mock
	private HueMessager hueMessager;

	@Test
	public void sendsOffRequestWhenCalled() throws Exception {
		handler.handle();
		verify(hueMessager).sendLightsOffRequest();
	}

	@Test
	public void returnsResponseWithNoText() {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech(), nullValue());
	}
}
