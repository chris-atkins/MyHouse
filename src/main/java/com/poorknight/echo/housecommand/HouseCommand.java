package com.poorknight.echo.housecommand;

public enum HouseCommand {
	AT_WORK_MODE("at-work-mode"),
	LIGHTS_ON("lights-on"),
	LIGHTS_OFF("lights-off");

	private final String commandAsString;

	HouseCommand(String commandAsString) {
		this.commandAsString = commandAsString;
	}

	/*package*/ String getCommandAsString() {
		return commandAsString;
	}
}
