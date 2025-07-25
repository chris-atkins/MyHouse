package com.poorknight.echo.lights.color;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.house.lights.LightColor;

public class DesiredColorTranslatorTest {

	private final DesiredColorTranslator translator = new DesiredColorTranslator();

	private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

	@Test
	public void translatesNormalCorrectly() {
		final JsonNode slots = buildJsonNodeWithDesiredColor("Normal");
		final LightColor lightColor = translator.translate(slots);
		assertThat(lightColor).isEqualTo(LightColor.NORMAL);
	}

	@Test
	public void translatesBlueCorrectly() {
		final JsonNode slots = buildJsonNodeWithDesiredColor("Blue");
		final LightColor lightColor = translator.translate(slots);
		assertThat(lightColor).isEqualTo(LightColor.BLUE);
	}

	@Test
	public void translatesRedCorrectly() {
		final JsonNode slots = buildJsonNodeWithDesiredColor("Red");
		final LightColor lightColor = translator.translate(slots);
		assertThat(lightColor).isEqualTo(LightColor.RED);
	}

	@Test
	public void throwsExceptionOnUnknownColorValue() {
		try {
			final JsonNode slots = buildJsonNodeWithDesiredColor("UnknownColor");
			translator.translate(slots);
			fail("expecting exception");
		} catch (RuntimeException e) {
			assertThat(e).isInstanceOf(LightColorRequestTranslationException.class);
		}
	}

	@Test
	public void throwsExceptionWhenNoDesiredColorNodeExists() {
		try {
			final JsonNode slots = buildJsonNodeWithNoDesiredColorSlot();
			translator.translate(slots);
			fail("expecting exception");
		} catch (RuntimeException e) {
			assertThat(e).isInstanceOf(LightColorRequestTranslationException.class);
		}
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
