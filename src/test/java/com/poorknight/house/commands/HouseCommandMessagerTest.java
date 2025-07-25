package com.poorknight.house.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.WebResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import javax.ws.rs.core.MediaType;

import static com.poorknight.house.commands.HouseCommand.AT_WORK_MODE;
import static com.poorknight.house.commands.HouseCommand.DIM_LIGHTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HouseCommandMessagerTest {

	private final static String houseCommandPath = "/house/command";

	@Mock
	private WebResource.Builder webResource;

	@Mock
	private WebResource.Builder builder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private HouseCommandMessager messager= new HouseCommandMessager();

	@Test
	public void putsDesiredCommandInCorrectFormat() {
		try(MockedStatic<WebResourceFactory> mockedFactory = mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(houseCommandPath)).thenReturn(webResource);

			when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
			when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);

			messager.requestHouseCommand(AT_WORK_MODE);
			verify(builder).put(eq(JsonNode.class), captor.capture());

			JsonNode sentRequest = captor.getValue();

			final JsonNode expectedRequest = JsonNodeFactory.instance.objectNode().put("command", AT_WORK_MODE.asPiString());
			assertThat(sentRequest).isEqualTo(expectedRequest);
		}
	}

	@Test
	public void returnsJsonResponseFromPi() {
		try(MockedStatic<WebResourceFactory> mockedFactory = mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(houseCommandPath)).thenReturn(webResource);

			when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
			when(builder.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
			TextNode responseFromPi = JsonNodeFactory.instance.textNode(RandomStringUtils.random(10));
			when(builder.put(eq(JsonNode.class), any(JsonNode.class))).thenReturn(responseFromPi);

			JsonNode response = messager.requestHouseCommand(DIM_LIGHTS);

			assertThat(response).isEqualTo(responseFromPi);
		}
	}
}