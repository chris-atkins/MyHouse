package com.poorknight.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.echo.EchoRequestHandlerFactory;
import com.poorknight.echo.EchoResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/")
public class EchoEndpoint {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "hi";
	}

	@GET
	@Path("/on")
	@Produces(MediaType.TEXT_PLAIN)
	public String getOn() {
		sendLightsRequestWithOn(true);
		return "lights on";
	}

	@GET
	@Path("/off")
	@Produces(MediaType.TEXT_PLAIN)
	public String getOff() {
		sendLightsRequestWithOn(false);
		return "ligths off";
	}

	private ClientResponse sendLightsRequestWithOn(final boolean lightsOn) {
		final Client client = Client.create();
		final WebResource webResource = client.resource("http://162.205.118.185:53335/api/6b1abf1f6e7157cc3843ee8b668d32d/lights/2/state");
		final ObjectNode data = JsonNodeFactory.instance.objectNode();
		data.set("on", JsonNodeFactory.instance.booleanNode(lightsOn));
		final ClientResponse response = webResource.type("application/json").put(ClientResponse.class, data);
		return response;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EchoResponse postEchoRequest(final JsonNode request) {
		return EchoRequestHandlerFactory.handlerFor(request).handle();
	}
}