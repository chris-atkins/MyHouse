package com.poorknight.echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.echo.hello.HelloRequestHandler;
import com.poorknight.echo.lights.LightsOffRequestHandler;
import com.poorknight.echo.lights.LightsOnRequestHandler;

@RunWith(JUnit4.class)
public class EchoRequestHandlerFactoryTest {

	@Test
	public void returnsHelloRequestHandlerWhenAppropriate() throws Exception {
		final JsonNode request = buildRequest("SayHi");
		final EchoRequestHandler handler = EchoRequestHandlerFactory.handlerFor(request);
		assertThat(handler, is(instanceOf(HelloRequestHandler.class)));
	}

	@Test
	public void returnsLightsOffRequestWhenAppropriate() throws Exception {
		final JsonNode request = buildRequest("LightsOff");
		final EchoRequestHandler handler = EchoRequestHandlerFactory.handlerFor(request);
		assertThat(handler, is(instanceOf(LightsOffRequestHandler.class)));
	}

	@Test
	public void returnsLightsOnRequestWhenAppropriate() throws Exception {
		final JsonNode request = buildRequest("LightsOn");
		final EchoRequestHandler handler = EchoRequestHandlerFactory.handlerFor(request);
		assertThat(handler, is(instanceOf(LightsOnRequestHandler.class)));
	}

	@Test(expected = RuntimeException.class)
	public void throwsExceptionForUnknownIntent() throws Exception {
		final JsonNode request = buildRequest("IDontKnowThisIntent");
		EchoRequestHandlerFactory.handlerFor(request);
	}

	private JsonNode buildRequest(final String intentName) {
		final ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
		rootNode.set("session", buildSessionNode());
		rootNode.set("request", buildRequestNode(intentName));
		return rootNode;
	}

	private JsonNode buildSessionNode() {
		final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		final ObjectNode sessionNode = nodeFactory.objectNode();
		sessionNode.set("sessionId", nodeFactory.textNode("SessionId.03639f14-fcf3-4eca-8de4-743d9428ee0c"));

		final ObjectNode applicationNode = nodeFactory.objectNode();
		applicationNode.set("applicationId", nodeFactory.textNode("amzn1.echo-sdk-ams.app.4382714e-1582-4f91-bf5a-261fcda24e81"));
		sessionNode.set("application", applicationNode);

		final ObjectNode userNode = nodeFactory.objectNode();
		userNode.set("userId", nodeFactory.textNode("amzn1.echo-sdk-account.AFEGUJ4EKDL5IGE6HH2IZR4AYOVSFQQW2WZR4YCYNJGDYFKPU47R6"));
		sessionNode.set("user", userNode);

		sessionNode.set("new", nodeFactory.booleanNode(true));
		return sessionNode;
	}

	private JsonNode buildRequestNode(final String intentName) {
		final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		final ObjectNode requestNode = nodeFactory.objectNode();
		requestNode.set("type", nodeFactory.textNode("IntentRequest"));
		requestNode.set("requestId", nodeFactory.textNode("EdwRequestId.6123c5ff-d54c-4ce1-9ba7-099f2872f3cb"));
		requestNode.set("timestamp", nodeFactory.textNode("2016-01-01T21:17:22Z"));

		final ObjectNode intentNode = nodeFactory.objectNode();
		intentNode.set("name", nodeFactory.textNode(intentName));
		intentNode.set("slots", nodeFactory.objectNode());
		requestNode.set("intent", intentNode);

		return requestNode;
	}
}
