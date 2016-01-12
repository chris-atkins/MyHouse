package com.poorknight.echo.lights.color;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(JUnit4.class)
public class DesiredColorTranslatorTest {

	private final DesiredColorTranslator translator = new DesiredColorTranslator();

	private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

	@Test
	public void translatesNormalCorrectly() throws Exception {
		final JsonNode slots = buildJsonNodeWithDesiredColor("Normal");
		final LightColor lightColor = translator.translate(slots);
		assertThat(lightColor, equalTo(LightColor.NORMAL));
	}

	@Test
	public void translatesBlueCorrectly() throws Exception {
		final JsonNode slots = buildJsonNodeWithDesiredColor("Blue");
		final LightColor lightColor = translator.translate(slots);
		assertThat(lightColor, equalTo(LightColor.BLUE));
	}

	@Test
	public void translatesRedCorrectly() throws Exception {
		final JsonNode slots = buildJsonNodeWithDesiredColor("Red");
		final LightColor lightColor = translator.translate(slots);
		assertThat(lightColor, equalTo(LightColor.RED));
	}

	@Test(expected = LightColorRequestTranslationException.class)
	public void throwsExceptionOnUnknownColorValue() throws Exception {
		final JsonNode slots = buildJsonNodeWithDesiredColor("UnknownColor");
		translator.translate(slots);
	}

	@Test(expected = LightColorRequestTranslationException.class)
	public void throwsExceptionWhenNoDesiredColorNodeExists() throws Exception {
		final JsonNode slots = buildJsonNodeWithNoDesiredColorSlot();
		translator.translate(slots);
	}

	private JsonNode buildJsonNodeWithDesiredColor(final String desiredColorString) {
		final ObjectNode slotsNode = nodeFactory.objectNode();
		final ObjectNode desiredColorNode = buildSlotNode("DesiredColor", desiredColorString);
		slotsNode.set("DesiredColor", desiredColorNode);

		return slotsNode;
	}

	private ObjectNode buildSlotNode(final String slotName, final String desiredColorString) {
		final ObjectNode desiredColorNode = nodeFactory.objectNode();
		desiredColorNode.set("name", nodeFactory.textNode(slotName));
		desiredColorNode.set("value", nodeFactory.textNode(desiredColorString));

		return desiredColorNode;
	}

	private JsonNode buildJsonNodeWithNoDesiredColorSlot() {
		final ObjectNode slotsNode = nodeFactory.objectNode();
		final ObjectNode desiredColorNode = buildSlotNode("SomeOtherSlot", "aValue");
		slotsNode.set("SomeOtherSlot", desiredColorNode);

		return slotsNode;
	}
}
