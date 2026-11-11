package com.poorknight.endpoints;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoRequestHandlerFactory;
import com.poorknight.echo.EchoResponse;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

@ExtendWith(MockitoExtension.class)
public class EchoEndpointTest {

	private final EchoEndpoint endpoint = new EchoEndpoint();

	@Mock
	private EchoRequestHandler handler;

	@Mock
	private JsonNode request;

	@Mock
	private EchoResponse expectedResponse;

	private MockedStatic<EchoRequestHandlerFactory> mockedFactory;

	@BeforeEach
	public void setUp() {
		mockedFactory = mockStatic(EchoRequestHandlerFactory.class);
		mockedFactory.when(() -> EchoRequestHandlerFactory.handlerFor(any())).thenReturn(handler);
	}

	@AfterEach
	public void tearDown() {
		mockedFactory.close();
	}

	@Test
	public void enpointReturnsResultFromHandlerRetrievedFromFactory() throws Exception {
		when(handler.handle()).thenReturn(expectedResponse);

		final EchoResponse actualResponse = endpoint.postEchoRequest(request);
		assertThat(actualResponse).isSameAs(expectedResponse);
	}

	@Test
	public void whenThePiTimesOutReading_SaysTheCommandMayStillHaveWorked() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException(new SocketTimeoutException("Read timed out")));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.MAY_STILL_HAVE_WORKED);
	}

	@Test
	public void whenThePiCannotBeConnectedTo_SaysItCouldNotReachTheHouse() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException(new ConnectException("Connection refused")));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void whenConnectingToThePiTimesOut_SaysItCouldNotReachTheHouse() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException(new SocketTimeoutException("Connect timed out")));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void whenAReadTimeoutIsWrappedMoreDeeply_StillSaysTheCommandMayHaveWorked() throws Exception {
		final Exception nested = new RuntimeException(new SocketTimeoutException("Read timed out"));
		when(handler.handle()).thenThrow(new ClientHandlerException(nested));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.MAY_STILL_HAVE_WORKED);
	}

	@Test
	public void whenTheClientFailsWithNoCause_SaysItMayStillHaveWorked() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException("no cause"));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.MAY_STILL_HAVE_WORKED);
	}

	@Test
	public void whenNginxReportsBadGateway_SaysItCouldNotReachTheHouse() throws Exception {
		final UniformInterfaceException piError = new UniformInterfaceException(responseWithStatus(502));
		when(handler.handle()).thenThrow(piError);

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void whenThePiReturnsAServerError_SaysItMayStillHaveWorked() throws Exception {
		final UniformInterfaceException piError = new UniformInterfaceException(responseWithStatus(500));
		when(handler.handle()).thenThrow(piError);

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.MAY_STILL_HAVE_WORKED);
	}

	@Test
	public void whenNginxTimesOutWaitingForThePi_SaysItMayStillHaveWorked() throws Exception {
		final UniformInterfaceException piError = new UniformInterfaceException(responseWithStatus(504));
		when(handler.handle()).thenThrow(piError);

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.MAY_STILL_HAVE_WORKED);
	}

	@Test
	public void whenThePiAppIsUnavailableBehindNginx_SaysItCouldNotReachTheHouse() throws Exception {
		final UniformInterfaceException piError = new UniformInterfaceException(responseWithStatus(503));
		when(handler.handle()).thenThrow(piError);

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void whenThePiRejectsTheSecret_SaysItCouldNotReachTheHouse() throws Exception {
		final UniformInterfaceException piError = new UniformInterfaceException(responseWithStatus(401));
		when(handler.handle()).thenThrow(piError);

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void whenThePiHostCannotBeResolved_SaysItCouldNotReachTheHouse() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException(new UnknownHostException("pi.example")));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void whenConnectTimeoutMessageIsLowercase_StillSaysItCouldNotReachTheHouse() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException(new SocketTimeoutException("connect timed out")));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void whenATimeoutHasAnUnexpectedMessage_ErrsTowardsMayStillHaveWorked() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException(new SocketTimeoutException("some future JDK wording")));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.MAY_STILL_HAVE_WORKED);
	}

	@Test
	public void whenThePiAnswersWithAnUnreadableBody_SaysItMayStillHaveWorked() throws Exception {
		final Exception parseFailure = catchThrowableOfType(() -> new ObjectMapper().readTree("{not json"), JsonProcessingException.class);
		when(handler.handle()).thenThrow(new ClientHandlerException(parseFailure));

		assertThat(speechOf(endpoint.postEchoRequest(request))).isEqualTo(EchoEndpoint.MAY_STILL_HAVE_WORKED);
	}

	@Test
	public void piFailuresAreHandled_ForRequestsWithAnIntentName() throws Exception {
		final JsonNode intentRequest = new ObjectMapper().readTree("{\"request\":{\"type\":\"IntentRequest\",\"intent\":{\"name\":\"LightsOn\"}}}");
		final UniformInterfaceException piError = new UniformInterfaceException(responseWithStatus(502));
		when(handler.handle()).thenThrow(piError);

		assertThat(speechOf(endpoint.postEchoRequest(intentRequest))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void piFailuresAreHandled_ForRequestsWithoutAnIntent() throws Exception {
		final JsonNode sessionEnded = new ObjectMapper().readTree("{\"request\":{\"type\":\"SessionEndedRequest\"}}");
		when(handler.handle()).thenThrow(new ClientHandlerException(new ConnectException("Connection refused")));

		assertThat(speechOf(endpoint.postEchoRequest(sessionEnded))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void piFailuresAreHandled_WhenTheIntentNameIsNotText() throws Exception {
		final JsonNode oddRequest = new ObjectMapper().readTree("{\"request\":{\"intent\":{\"name\":5}}}");
		when(handler.handle()).thenThrow(new ClientHandlerException(new ConnectException("Connection refused")));

		assertThat(speechOf(endpoint.postEchoRequest(oddRequest))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void piFailuresAreHandled_WhenTheRequestIsNotAnObject() throws Exception {
		final JsonNode oddRequest = new ObjectMapper().readTree("{\"request\":\"just a string\"}");
		when(handler.handle()).thenThrow(new ClientHandlerException(new ConnectException("Connection refused")));

		assertThat(speechOf(endpoint.postEchoRequest(oddRequest))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void piFailuresAreHandled_ForANullRequest() throws Exception {
		when(handler.handle()).thenThrow(new ClientHandlerException(new ConnectException("Connection refused")));

		assertThat(speechOf(endpoint.postEchoRequest(null))).isEqualTo(EchoEndpoint.COULD_NOT_REACH_HOUSE);
	}

	@Test
	public void otherFailuresStillPropagate() throws Exception {
		final IllegalStateException failure = new IllegalStateException("a bug, not the Pi");
		when(handler.handle()).thenThrow(failure);

		assertThat(catchThrowable(() -> endpoint.postEchoRequest(request))).isSameAs(failure);
	}

	private static ClientResponse responseWithStatus(final int status) {
		final ClientResponse response = mock(ClientResponse.class);
		when(response.getStatus()).thenReturn(status);
		return response;
	}

	private static String speechOf(final EchoResponse response) {
		return response.getResponse().getOutputSpeech().getText();
	}
}
