package com.poorknight.house.pi;

import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PiMessagerTest {

	private PiMessager piMessager= new PiMessager();

	@Mock
	private WebResource.Builder webResource;

	@Mock
	private Builder webResourceBuilder;

	private final String winkPath = "/wink";

	private final String expectedRestType = "application/json";

	@Test
	public void sendsWinkRequestWhenCalled() throws Exception {
		try(MockedStatic<WebResourceFactory> mockedFactory = Mockito.mockStatic(WebResourceFactory.class)) {
			mockedFactory.when(() -> WebResourceFactory.buildSecuredHomeWebResource(winkPath)).thenReturn(webResource);
			when(webResource.type(expectedRestType)).thenReturn(webResourceBuilder);

			piMessager.sendWinkRequest();
			verify(webResourceBuilder).get(ClientResponse.class);
		}
	}
}
