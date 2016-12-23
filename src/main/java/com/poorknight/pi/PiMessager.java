package com.poorknight.pi;

import com.poorknight.server.WebResourceFactory;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PiMessager {

	private WebResource buildWebResource() {
		return WebResourceFactory.buildSecuredHomeWebResource("/wink");
	}

	public void sendWinkRequest() {
		buildWebResource().type("application/json").get(ClientResponse.class);
	}
}
