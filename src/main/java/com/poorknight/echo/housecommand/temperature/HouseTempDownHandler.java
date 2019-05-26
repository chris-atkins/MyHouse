package com.poorknight.echo.housecommand.temperature;

import com.fasterxml.jackson.databind.JsonNode;
import com.poorknight.echo.EchoRequestHandler;
import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.housecommand.HouseCommandMessager;

import static com.poorknight.echo.housecommand.HouseCommand.HOUSE_TEMP_DOWN;

public class HouseTempDownHandler implements EchoRequestHandler {

	private HouseCommandMessager houseCommandMessager;

	private TempAdjustmentResponseBuilder tempAdjustmentResponseBuilder;

	public HouseTempDownHandler(HouseCommandMessager houseCommandMessager, TempAdjustmentResponseBuilder tempAdjustmentResponseBuilder) {
		this.houseCommandMessager = houseCommandMessager;
		this.tempAdjustmentResponseBuilder = tempAdjustmentResponseBuilder;
	}

	@Override
	public EchoResponse handle() {
		JsonNode responseFromPi = houseCommandMessager.requestHouseCommand(HOUSE_TEMP_DOWN);
		return tempAdjustmentResponseBuilder.buildResponse(responseFromPi);
	}
}
