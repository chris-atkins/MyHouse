package com.poorknight.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoRequestHandlerFactory;
import com.poorknight.echo.EchoResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class EchoEndpoint {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EchoResponse postEchoRequest(final JsonNode request) {
		return EchoRequestHandlerFactory.handlerFor(request).handle();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getEchoRequest(final JsonNode request) {
		return "you have hit the endpoint";
	}
}