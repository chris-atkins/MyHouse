package com.poorknight.echo.housecommand.temperature;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.house.commands.HouseCommand;
import com.poorknight.house.commands.HouseCommandMessager;

public class HouseTempUpHandler implements EchoRequestHandler {

	private HouseCommandMessager houseCommandMessager;
	private TempAdjustmentResponseBuilder responseBuilder;

	public HouseTempUpHandler(HouseCommandMessager houseCommandMessager, TempAdjustmentResponseBuilder responseBuilder) {
		this.houseCommandMessager = houseCommandMessager;
		this.responseBuilder = responseBuilder;
	}

	@Override
	public EchoResponse handle() {
		JsonNode responseFromPi = houseCommandMessager.requestHouseCommand(HouseCommand.HOUSE_TEMP_UP);
		return responseBuilder.buildResponse(responseFromPi);
	}
}
