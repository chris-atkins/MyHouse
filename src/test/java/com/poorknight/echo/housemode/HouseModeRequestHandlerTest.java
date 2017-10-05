package com.poorknight.echo.housemode;

import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.EchoResponseData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.poorknight.echo.housemode.HouseMode.AT_WORK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HouseModeRequestHandlerTest {

	private HouseModeRequestHandler handler;

	@Mock
	private HouseModeMessager houseModeMessager;

	@Mock
	private HouseModeResponseBuilder houseModeResponseBuilder;

	private HouseMode houseMode = AT_WORK;

	@Before
	public void setUp() throws Exception {
		handler = new HouseModeRequestHandler(houseMode, houseModeMessager, houseModeResponseBuilder);
	}

	@Test
	public void whenARequestIsHandled_TheHouseModeMessagerIsCalled_AndTheCorrectResponseIsReturned() throws Exception {
		final String speechResponse = RandomStringUtils.random(20);
		when(houseModeResponseBuilder.buildHouseModeAlexaResponse()).thenReturn(speechResponse);

		final EchoResponse echoResponse = handler.handle();

		verify(houseModeMessager).requestHouseMode(houseMode);

		final EchoResponseData response = echoResponse.getResponse();
		assertThat(response.getOutputSpeech().getText()).isEqualTo(speechResponse);
		assertThat(response.getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getShouldEndSession()).isTrue();
	}
}