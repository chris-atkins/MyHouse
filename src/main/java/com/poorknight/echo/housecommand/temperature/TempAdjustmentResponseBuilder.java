package com.poorknight.echo.housecommand.temperature;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.housecommand.HouseCommand;

import static com.poorknight.echo.housecommand.HouseCommandResult.FAILURE;
import static com.poorknight.echo.housecommand.HouseCommandResult.NO_CHANGE;

public class TempAdjustmentResponseBuilder {

	public EchoResponse buildResponse(JsonNode commandResponseFromPi) {
		String result = commandResponseFromPi.get("result").asText();
		if (FAILURE.asPiString().equals(result)) {
			return buildFailureMessage(commandResponseFromPi);
		}

		if (NO_CHANGE.asPiString().equals(result)) {
			return buildNoChangeMessage(commandResponseFromPi);
		}

		return buildSuccessResponse(commandResponseFromPi);
	}

	private EchoResponse buildFailureMessage(JsonNode commandResponseFromPi) {
		String upOrDown = findUpOrDownString(commandResponseFromPi);
		String reason = commandResponseFromPi.get("failure-reason").asText();
		return EchoResponse.responseWithSpeech("Your house could not turn the temp " + upOrDown + ". " + reason);
	}

	private EchoResponse buildNoChangeMessage(JsonNode commandResponseFromPi) {
		String coldOrHot = findColdOrHotString(commandResponseFromPi);
		return EchoResponse.responseWithSpeech("Don't you think it's " + coldOrHot + " enough already?");
	}

	private EchoResponse buildSuccessResponse(JsonNode commandResponseFromPi) {
		double newTemp = commandResponseFromPi.get("target-temp").asDouble();
		String upOrDown = findUpOrDownString(commandResponseFromPi);
		String acOrFurnace = findAcOrFurnaceString(commandResponseFromPi);

		return EchoResponse.responseWithSpeech(acOrFurnace + " turned " + upOrDown + " to " + newTemp + ".");
	}

	private String findUpOrDownString(JsonNode commandResponseFromPi) {
		HouseCommand command = HouseCommand.fromPiString(commandResponseFromPi.get("command").asText());
		String upOrDown = "up";
		if (command == HouseCommand.HOUSE_TEMP_DOWN) {
			upOrDown = "down";
		}
		return upOrDown;
	}

	private String findAcOrFurnaceString(JsonNode commandResponseFromPi) {
		HouseTemperatureMode temperatureMode = HouseTemperatureMode.fromPiString(commandResponseFromPi.get("temperature-mode").asText());
		if (temperatureMode == HouseTemperatureMode.AC) {
			return "AC";
		}
		return "Furnace";
	}

	private String findColdOrHotString(JsonNode commandResponseFromPi) {
		HouseCommand command = HouseCommand.fromPiString(commandResponseFromPi.get("command").asText());
		if (command == HouseCommand.HOUSE_TEMP_DOWN) {
			return "cold";
		}
		return "hot";
	}
}
