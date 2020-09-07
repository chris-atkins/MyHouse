package com.poorknight.house.commands;


public enum HouseCommandResult {

	SUCCESS("success"),
	NO_CHANGE("no-change"),
	FAILURE("failure"),;

	private String piString;

	HouseCommandResult(String piString) {

		this.piString = piString;
	}

	public static HouseCommandResult fromPiString(String stringToParse) {
		for (HouseCommandResult value : HouseCommandResult.values()) {
			if (value.piString.equals(stringToParse)) {
				return value;
			}
		}
		throw new RuntimeException("Cannot translate string into HouseCommandResult. The passed string does not represent a valid HouseCommandResult: " + stringToParse);
	}

	public String asPiString() {
		return this.piString;
	}
}
