package com.poorknight.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoRequestHandlerFactory;
import com.poorknight.echo.EchoResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EchoRequestHandlerFactory.class)
public class EchoEndpointTest {

	private final EchoEndpoint endpoint = new EchoEndpoint();

	@Mock
	private EchoRequestHandler handler;

	@Mock
	private JsonNode request;

	@Mock
	private EchoResponse expectedResponse;

	@Before
	public void setup() {
		PowerMockito.mockStatic(EchoRequestHandlerFactory.class);
	}

	@Test
	public void enpointReturnsResultFromHandlerRetrievedFromFactory() throws Exception {
		when(EchoRequestHandlerFactory.handlerFor(request)).thenReturn(handler);
		when(handler.handle()).thenReturn(expectedResponse);

		final EchoResponse actualResponse = endpoint.postEchoRequest(request);
		assertThat(actualResponse, is(expectedResponse));
	}
}
