package com.poorknight.echo.lights.color;

public enum LightColor {
	NORMAL("normal"), BLUE("blue"), RED("red");

	private String name;

	private LightColor(final String name) {
		this.name = name;
	}

	public static LightColor fromString(final String stringToMatch) {
		final String potentialMatch = stringToMatch.toLowerCase();
		for (final LightColor color : LightColor.values()) {
			if (color.name.equals(potentialMatch)) {
				return color;
			}
		}
		throw new RuntimeException("Unable to translate the string '" + stringToMatch + "' into a LightColor.");
	}
}
