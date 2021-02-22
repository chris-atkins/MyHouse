package com.poorknight.house.commands;

public enum HouseCommand {
	AT_WORK_MODE("at-work-mode"),
	LIGHTS_ON("lights-on"),
	LIGHTS_OFF("lights-off"),
	OUTSIDE_LIGHTS_ON("outside-lights-on"),
	OUTSIDE_LIGHTS_OFF("outside-lights-off"),
	DIM_LIGHTS("dim-lights"),
	HOUSE_TEMP_UP("house-temp-up"),
	HOUSE_TEMP_DOWN("house-temp-down"),
	BASEMENT_OFF("basement-off"),
	BASEMENT_DIM("basement-dim"),
	BASEMENT_ON("basement-on"),
	DINING_LIGHTS_BRIGHT("dining-lights-bright"),
	BEDROOM_ON("bedroom-on"),
	BEDROOM_OFF("bedroom-off");

	private final String piString;

	HouseCommand(String piString) {
		this.piString = piString;
	}

	public static HouseCommand fromPiString(String stringToParse) {
		for (HouseCommand command : HouseCommand.values()) {
			if (command.piString.equals(stringToParse)) {
				return command;
			}
		}
		throw new RuntimeException("Cannot translate string into HouseCommand. The passed string does not represent a valid HouseCommand: " + stringToParse);
	}

	/*package*/
	public String asPiString() {
		return piString;
	}
}
