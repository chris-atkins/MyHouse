package com.poorknight.echo.lights.on;

import com.poorknight.echo.EchoResponse;
import com.poorknight.lights.HueMessager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LightsOnRequestHandlerTest {

	@InjectMocks
	private LightsOnRequestHandler handler;

	@Mock
	private HueMessager hueMessager;

	@Test
	public void sendsOffRequestWhenCalled() throws Exception {
		handler.handle();
		verify(hueMessager).sendLightsOnRequest();
	}

	@Test
	@Ignore
	public void returnsResponseWithNoText() {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech(), nullValue());
	}
}
