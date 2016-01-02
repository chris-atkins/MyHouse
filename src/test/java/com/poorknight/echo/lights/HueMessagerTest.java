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

	@Before
	public void setup() {
		PowerMockito.mockStatic(Client.class);
		when(Client.create()).thenReturn(client);
		when(client.resource(Mockito.anyString())).thenReturn(webResource);
		when(webResource.type(Mockito.anyString())).thenReturn(webResourceBuilder);
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
