package com.poorknight.echo.housemode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.EchoResponseData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.poorknight.echo.housemode.HouseMode.AT_WORK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HouseModeRequestHandlerTest {

	private HouseModeRequestHandler handler;

	@Mock
	private HouseModeMessager houseModeMessager;
	private HouseMode houseMode = AT_WORK;

	@Before
	public void setUp() throws Exception {
		handler = new HouseModeRequestHandler(houseMode, houseModeMessager);
	}

	@Test
	public void whenARequestIsHandled_TheHouseModeMessagerIsCalled_AndTheCorrectResponseIsReturned() throws Exception {
		final EchoResponse echoResponse = handler.handle();

		verify(houseModeMessager).requestHouseMode(houseMode);

		final EchoResponseData response = echoResponse.getResponse();
		assertThat(response.getOutputSpeech().getText()).isEqualTo("Have a good day.");
		assertThat(response.getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getShouldEndSession()).isTrue();
	}

	@Test
	public void name() throws Exception {

		final EchoResponse echoResponse = EchoResponse.responseWithSpeech("Have a good day.");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(echoResponse);
		System.out.println(json);

	}
}