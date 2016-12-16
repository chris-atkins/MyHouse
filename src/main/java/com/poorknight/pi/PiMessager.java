package com.poorknight.pi;

import com.poorknight.settings.Environment;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PiMessager {

	private final Client client;
	private final WebResource webResource;

	public PiMessager() {
		client = Client.create();
		final String winkEndpointUrl = Environment.getEnvironmentVariable("HOUSE_URL") + "/wink";
		webResource = client.resource(winkEndpointUrl);
	}

	public void sendWinkRequest() {
		webResource.type("application/json").get(ClientResponse.class);
	}
}
