package com.poorknight.echo.housecommand.combination;

import java.util.Random;

public class GoingToWorkResponseBuilder {

	private Random randomNumberGenerator = new Random();

	/*package*/ String buildHouseCommandAlexaResponse() {
		int randomOfTen = randomNumberGenerator.nextInt(10);

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
