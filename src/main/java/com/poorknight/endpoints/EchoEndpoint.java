package com.poorknight.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoRequestHandlerFactory;
import com.poorknight.echo.EchoResponse;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;
import java.util.Locale;

@Path("/")
public class EchoEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(EchoEndpoint.class);

	static final String COULD_NOT_REACH_HOUSE = "Sorry, I couldn't reach the house.";
	// A read timeout doesn't mean the command failed - the Pi often finishes it anyway - and
	// commands like house-temp-up aren't idempotent, so don't invite a retry.
	static final String HOUSE_SLOW_TO_RESPOND = "The house is slow to respond. That may still have worked.";

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EchoResponse postEchoRequest(final JsonNode request) {
		// Answer Pi failures ourselves; an uncaught exception makes Alexa say "the requested skill did not provide a valid response".
		try {
			return EchoRequestHandlerFactory.handlerFor(request).handle();
		} catch (final UniformInterfaceException e) {
			logger.error("Pi returned HTTP {} for Alexa intent {}", e.getResponse().getStatus(), intentNameOf(request), e);
			return EchoResponse.responseWithSpeech(COULD_NOT_REACH_HOUSE);
		} catch (final ClientHandlerException e) {
			if (isReadTimeout(e)) {
				logger.error("Pi timed out responding to Alexa intent {}", intentNameOf(request), e);
				return EchoResponse.responseWithSpeech(HOUSE_SLOW_TO_RESPOND);
			}
			logger.error("Could not reach the Pi for Alexa intent {}", intentNameOf(request), e);
			return EchoResponse.responseWithSpeech(COULD_NOT_REACH_HOUSE);
		}
	}

	private static boolean isReadTimeout(final Throwable e) {
		for (Throwable cause = e; cause != null; cause = cause.getCause()) {
			if (cause instanceof SocketTimeoutException && cause.getMessage() != null
					&& cause.getMessage().toLowerCase(Locale.ROOT).contains("read timed out")) {
				return true;
			}
		}
		return false;
	}

	// Only used for logging, so it must never throw: the fallback response depends on it.
	private static String intentNameOf(final JsonNode request) {
		JsonNode node = request;
		for (final String field : new String[] {"request", "intent", "name"}) {
			node = node == null ? null : node.get(field);
		}
		return node != null && node.isTextual() ? node.asText() : "<none>";
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getEchoRequest(final JsonNode request) {
		return "you have hit the endpoint.\n\n\n\nLocal sunset/sunrise timings provided, with much thanks, by: https://sunrise-sunset.org/api";
	}
}