package com.poorknight.echo.housecommand.combination;

import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.EchoResponseData;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.poorknight.house.commands.HouseCommand.AT_WORK_MODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoingToWorkRequestHandlerTest {

	private GoingToWorkRequestHandler handler;

	@Mock
	private HouseCommandMessager houseCommandMessager;

	@Mock
	private GoingToWorkResponseBuilder goingToWorkResponseBuilder;

	private HouseCommand houseCommand = AT_WORK_MODE;

	@Before
	public void setUp() throws Exception {
		handler = new GoingToWorkRequestHandler(houseCommandMessager, goingToWorkResponseBuilder);
	}

	@Test
	public void whenARequestIsHandled_TheHouseCommandMessagerIsCalled_AndTheCorrectResponseIsReturned() throws Exception {
		final String speechResponse = RandomStringUtils.random(20);
		when(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).thenReturn(speechResponse);

		final EchoResponse echoResponse = handler.handle();

		verify(houseCommandMessager).requestHouseCommand(houseCommand);

		final EchoResponseData response = echoResponse.getResponse();
		assertThat(response.getOutputSpeech().getText()).isEqualTo(speechResponse);
		assertThat(response.getOutputSpeech().getType()).isEqualTo("PlainText");
		assertThat(response.getShouldEndSession()).isTrue();
	}
}