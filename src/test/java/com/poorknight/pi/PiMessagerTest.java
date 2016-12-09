package com.poorknight.pi;

import com.fasterxml.jackson.databind.JsonNode;
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
@PrepareForTest(Client.class)
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

	private final String expectedUrl = "https://75.38.163.141:35553/wink";
	private final String expectedRestType = "application/json";

	@Before
	public void setup() {
		PowerMockito.mockStatic(Client.class);
		when(Client.create()).thenReturn(client);
		when(client.resource(expectedUrl)).thenReturn(webResource);
		when(webResource.type(expectedRestType)).thenReturn(webResourceBuilder);
		piMessager = new PiMessager();
	}

	@Test
	public void sendsWinkRequestWhenCalled() throws Exception {
		piMessager.sendWinkRequest();
		verify(webResourceBuilder).get(ClientResponse.class);
	}
}
