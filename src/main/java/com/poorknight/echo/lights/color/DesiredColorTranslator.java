package com.poorknight.echo.lights.color;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.lights.LightColor;

public class DesiredColorTranslator {

	public LightColor translate(final JsonNode slots) {
		try {
			return performTranslation(slots);

		} catch (final Exception e) {
			throw new LightColorRequestTranslationException(e);
		}
	}

	private LightColor performTranslation(final JsonNode slots) {
		final String desiredColorString = slots.get("DesiredColor").get("value").asText();
		return LightColor.fromString(desiredColorString);
	}
}
