package com.poorknight.pi;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PiMessager {

	private final Client client;
	private final WebResource webResource;

	public PiMessager() {
		client = Client.create();
		webResource = client.resource("https://75.38.163.141:35553/wink");
	}

	public void sendWinkRequest() {
		webResource.type("application/json").get(ClientResponse.class);
	}
}
