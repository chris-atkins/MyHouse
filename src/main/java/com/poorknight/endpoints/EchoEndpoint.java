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
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Set;

@Path("/")
public class EchoEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(EchoEndpoint.class);

	// Only said when we know the command never ran, because it invites a retry.
	static final String COULD_NOT_REACH_HOUSE = "Sorry, I couldn't reach the house.";
	// Said whenever the request may have reached the Pi: a timed-out or failed command often completes
	// anyway, and some (house-temp-up/down) aren't safe to repeat. Unknown failures land here too.
	static final String MAY_STILL_HAVE_WORKED = "The house didn't confirm that. It may still have worked.";

	// 401: rejected before running. 502/503: nginx on the Pi couldn't hand the request to the app.
	private static final Set<Integer> STATUSES_WHERE_COMMAND_DID_NOT_RUN = Set.of(401, 502, 503);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EchoResponse postEchoRequest(final JsonNode request) {
		// Answer Pi failures ourselves; an uncaught exception makes Alexa say "the requested skill did not provide a valid response".
		try {
			return EchoRequestHandlerFactory.handlerFor(request).handle();
		} catch (final UniformInterfaceException e) {
			final int status = statusOf(e);
			logger.error("Pi returned HTTP {} for Alexa intent {}", status, intentNameOf(request), e);
			return EchoResponse.responseWithSpeech(STATUSES_WHERE_COMMAND_DID_NOT_RUN.contains(status) ? COULD_NOT_REACH_HOUSE : MAY_STILL_HAVE_WORKED);
		} catch (final ClientHandlerException e) {
			logger.error("Pi call failed for Alexa intent {}", intentNameOf(request), e);
			return EchoResponse.responseWithSpeech(neverReachedThePi(e) ? COULD_NOT_REACH_HOUSE : MAY_STILL_HAVE_WORKED);
		}
	}

	private static boolean neverReachedThePi(final Throwable e) {
		for (Throwable cause = e; cause != null; cause = cause.getCause()) {
			if (cause instanceof ConnectException || cause instanceof NoRouteToHostException || cause instanceof UnknownHostException) {
				return true;
			}
			if (cause instanceof SocketTimeoutException) {
				// The JDK says "Connect timed out" vs "Read timed out"; anything else errs towards "may still have worked".
				return cause.getMessage() != null && cause.getMessage().toLowerCase(Locale.ROOT).contains("connect timed out");
			}
		}
		return false;
	}

	private static int statusOf(final UniformInterfaceException e) {
		return e.getResponse() == null ? -1 : e.getResponse().getStatus();
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