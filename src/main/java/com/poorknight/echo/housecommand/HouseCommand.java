package com.poorknight.echo.housecommand;

public enum HouseCommand {
	AT_WORK_MODE("at-work-mode"),
	LIGHTS_ON("lights-on"),
	LIGHTS_OFF("lights-off"),
	OUTSIDE_LIGHTS_ON("outside-lights-on"),
	OUTSIDE_LIGHTS_OFF("outside-lights-off"),
	DIM_LIGHTS("dim-lights"),
	HOUSE_TEMP_UP("house-temp-up"),
	HOUSE_TEMP_DOWN("house-temp-down");

	private final String commandAsString;

	HouseCommand(String commandAsString) {
		this.commandAsString = commandAsString;
	}

	/*package*/ String getCommandAsString() {
		return commandAsString;
	}
}
