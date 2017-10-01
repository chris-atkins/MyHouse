package com.poorknight.echo.housemode;

public enum HouseMode {
	AT_WORK("at-work");

	private final String modeAsString;

	HouseMode(String modeAsString) {
		this.modeAsString = modeAsString;
	}

	/*package*/ String getModeAsString() {
		return modeAsString;
	}
}
