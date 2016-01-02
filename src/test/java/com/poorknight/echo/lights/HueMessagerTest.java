package com.poorknight.echo.lights;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Client.class)
public class HueMessagerTest {

	@InjectMocks
	private HueMessager hueMessager;

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	private Builder webResourceBuilder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private final String expectedUrl = "http://162.205.118.185:53335/api/6b1abf1f6e7157cc3843ee8b668d32d/groups/0/action";
	private final String expectedRestType = "application/json";

	@Before
	public void setup() {
		PowerMockito.mockStatic(Client.class);
		when(Client.create()).thenReturn(client);
		when(client.resource(expectedUrl)).thenReturn(webResource);
		when(webResource.type(expectedRestType)).thenReturn(webResourceBuilder);
	}

	@Test
	public void sendsOffRequestWhenCalled() throws Exception {
		hueMessager.sendLightsOffRequest();
		final JsonNode sentRequest = captureSentHueArgument();
		assertThat(sentRequest.get("on").asBoolean(), equalTo(false));
	}

	@Test
	public void sendsOnRequestWhenCalled() throws Exception {
		hueMessager.sendLightsOnRequest();
		final JsonNode sentRequest = captureSentHueArgument();
		assertThat(sentRequest.get("on").asBoolean(), equalTo(true));
	}

	private JsonNode captureSentHueArgument() {
		verify(webResourceBuilder).put(Mockito.eq(ClientResponse.class), captor.capture());
		final JsonNode sentRequest = captor.getValue();
		return sentRequest;
	}
}
