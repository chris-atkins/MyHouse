package com.poorknight.echo.housecommand.temperature;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HouseTempUpHandlerTest {

	@InjectMocks
	private HouseTempUpHandler handler;

	@Mock
	private HouseCommandMessager houseCommandMessager;

	@Mock
	private TempAdjustmentResponseBuilder responseBuilder;

	@Test
	public void sendsCommandToPi_AndReturnsResponseFromResponseBuilder() {
		JsonNode responseFromPi = JsonNodeFactory.instance.textNode(RandomStringUtils.random(10));
		EchoResponse responseToUser = EchoResponse.responseWithSpeech(RandomStringUtils.random(15));

		when(houseCommandMessager.requestHouseCommand(HouseCommand.HOUSE_TEMP_UP)).thenReturn(responseFromPi);
		when(responseBuilder.buildResponse(responseFromPi)).thenReturn(responseToUser);

		EchoResponse result = handler.handle();

		assertThat(result).isEqualTo(responseToUser);
	}
}