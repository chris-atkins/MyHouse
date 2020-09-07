package com.poorknight.echo.pi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.poorknight.echo.EchoResponse;
import com.poorknight.house.pi.PiMessager;

@RunWith(MockitoJUnitRunner.class)
public class WinkRequestHandlerTest {

	@InjectMocks
	private WinkRequestHandler handler;

	@Mock
	private PiMessager piMessager;

	@Test
	public void callsPiMessage() throws Exception {
		handler.handle();
		verify(piMessager).sendWinkRequest();
	}

	@Test
	public void respondsWithANoOutputSpeechResponse() throws Exception {
		final EchoResponse response = handler.handle();
		assertThat(response.getResponse().getOutputSpeech(), nullValue());
	}
}
