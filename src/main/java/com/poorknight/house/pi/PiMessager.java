package com.poorknight.house.pi;

import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PiMessager {

	private WebResource.Builder buildWebResource() {
		return WebResourceFactory.buildSecuredHomeWebResource("/wink");
	}

	public void sendWinkRequest() {
		buildWebResource().type("application/json").get(ClientResponse.class);
	}
}
