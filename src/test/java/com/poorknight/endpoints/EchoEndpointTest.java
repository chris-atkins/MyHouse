package com.poorknight.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoRequestHandlerFactory;
import com.poorknight.echo.EchoResponse;

@ExtendWith(MockitoExtension.class)
public class EchoEndpointTest {

	private final EchoEndpoint endpoint = new EchoEndpoint();

	@Mock
	private EchoRequestHandler handler;

	@Mock
	private JsonNode request;

	@Mock
	private EchoResponse expectedResponse;

	@Test
	public void enpointReturnsResultFromHandlerRetrievedFromFactory() throws Exception {
		try (MockedStatic<EchoRequestHandlerFactory> mockedFactory = mockStatic(EchoRequestHandlerFactory.class)) {

			mockedFactory.when(() -> EchoRequestHandlerFactory.handlerFor(request)).thenReturn(handler);
			when(handler.handle()).thenReturn(expectedResponse);

			final EchoResponse actualResponse = endpoint.postEchoRequest(request);
			assertThat(actualResponse, is(expectedResponse));
		}
	}
}
