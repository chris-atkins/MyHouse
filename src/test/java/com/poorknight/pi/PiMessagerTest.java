package com.poorknight.pi;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.settings.Environment;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Client.class, Environment.class})
public class PiMessagerTest {

	private PiMessager piMessager;

	@Mock
	private Client client;

	@Mock
	private WebResource webResource;

	@Mock
	private Builder webResourceBuilder;

	@Captor
	private ArgumentCaptor<JsonNode> captor;

	private final String houseUrl = "houseUrl";
	private final String winkEndpoint = "/wink";
	private final String expectedEndpoint = houseUrl + winkEndpoint;

	private final String expectedRestType = "application/json";

	@Before
	public void setup() {
		PowerMockito.mockStatic(Client.class);
		PowerMockito.mockStatic(Environment.class);
		when(Client.create()).thenReturn(client);
		when(client.resource(expectedEndpoint)).thenReturn(webResource);
		when(webResource.type(expectedRestType)).thenReturn(webResourceBuilder);
		when(Environment.getEnvironmentVariable("HOUSE_URL")).thenReturn(houseUrl);
		piMessager = new PiMessager();
	}

	@Test
	public void sendsWinkRequestWhenCalled() throws Exception {
		piMessager.sendWinkRequest();
		verify(webResourceBuilder).get(ClientResponse.class);
	}
}
