package com.poorknight.echo.housemode;

import java.util.Random;

public class HouseModeResponseBuilder {


	/*package*/ String buildHouseModeAlexaResponse() {
		int randomOfTen = new Random().nextInt(10);

		if (randomOfTen < 3) {
			return "Have a good day.";
		}

		if (randomOfTen < 6) {
			return "Have a nice day.";
		}

		if (randomOfTen < 9) {
			return "See ya.";
		}

		return "Fuck you.";
	}
}
