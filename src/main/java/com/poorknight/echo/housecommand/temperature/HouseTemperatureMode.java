package com.poorknight.echo.housecommand.temperature;

public enum HouseTemperatureMode {

	AC("AC"),
	FURNACE("furnace");

	private String piString;

	HouseTemperatureMode(String piString) {
		this.piString = piString;
	}

	public static HouseTemperatureMode fromPiString(String stringToParse) {
		for (HouseTemperatureMode value : HouseTemperatureMode.values()) {
			if (value.piString.equals(stringToParse)) {
				return value;
			}
		}
		throw new RuntimeException("Cannot translate string into HouseTemperatureMode. The passed string does not represent a valid HouseTemperatureMode: " + stringToParse);
	}

	public String asPiString() {
		return this.piString;
	}
}
