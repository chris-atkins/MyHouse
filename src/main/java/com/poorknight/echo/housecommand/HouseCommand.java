package com.poorknight.echo.housecommand;

public enum HouseCommand {
	AT_WORK_MODE("at-work-mode");

	private final String commandAsString;

	HouseCommand(String commandAsString) {
		this.commandAsString = commandAsString;
	}

	/*package*/ String getCommandAsString() {
		return commandAsString;
	}
}
