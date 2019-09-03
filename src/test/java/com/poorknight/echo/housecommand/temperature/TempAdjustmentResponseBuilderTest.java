package com.poorknight.echo.housecommand.temperature;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poorknight.echo.EchoResponse;
import com.poorknight.echo.housecommand.HouseCommandResult;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.poorknight.echo.housecommand.HouseCommand.HOUSE_TEMP_DOWN;
import static com.poorknight.echo.housecommand.HouseCommand.HOUSE_TEMP_UP;
import static com.poorknight.echo.housecommand.HouseCommandResult.*;
import static com.poorknight.echo.housecommand.temperature.HouseTemperatureMode.AC;
import static com.poorknight.echo.housecommand.temperature.HouseTemperatureMode.FURNACE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class TempAdjustmentResponseBuilderTest {

	@Test
	public void tempDown_ForAC_WithSuccess_BuildsResponseWithMessage() {
		double newTemp = RandomUtils.nextDouble(60, 70);
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_DOWN.asPiString())
				.put("result", SUCCESS.asPiString())
				.put("temperature-mode", AC.asPiString())
				.put("target-temp", newTemp);

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("AC turned down to " + newTemp + ".");
	}

	@Test
	public void tempDown_ForFurnace_WithSuccess_BuildsResponseWithMessage() {
		double newTemp = RandomUtils.nextDouble(60, 70);
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_DOWN.asPiString())
				.put("result", SUCCESS.asPiString())
				.put("temperature-mode", FURNACE.asPiString())
				.put("target-temp", newTemp);

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("Furnace turned down to " + newTemp + ".");
	}

	@Test
	public void tempDown_WithNoChange_BuildsResponseWithMessage() {
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_DOWN.asPiString())
				.put("result", NO_CHANGE.asPiString());

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("Don't you think it's cold enough already?");
	}

	@Test
	public void tempDown_WithFailure_BuildsResponseWithMessage() {
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_DOWN.asPiString())
				.put("result", FAILURE.asPiString())
				.put("failure-reason", "some reason");

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("Your house could not turn the temp down. some reason");
	}

	@Test
	public void tempUp_ForAC_WithSuccess_BuildsResponseWithMessage() {
		double newTemp = RandomUtils.nextDouble(60, 70);
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_UP.asPiString())
				.put("result", SUCCESS.asPiString())
				.put("temperature-mode", AC.asPiString())
				.put("target-temp", newTemp);

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("AC turned up to " + newTemp + ".");

	}

	@Test
	public void tempUp_ForFurnace_WithSuccess_BuildsResponseWithMessage() {
		double newTemp = RandomUtils.nextDouble(60, 70);
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_UP.asPiString())
				.put("result", SUCCESS.asPiString())
				.put("temperature-mode", FURNACE.asPiString())
				.put("target-temp", newTemp);

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("Furnace turned up to " + newTemp + ".");
	}

	@Test
	public void tempChange_RemovesTrailingZeroDecimalPlace() {
		double newTemp = 60.0;
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_UP.asPiString())
				.put("result", SUCCESS.asPiString())
				.put("temperature-mode", AC.asPiString())
				.put("target-temp", newTemp);

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("AC turned up to 60.");
	}

	@Test
	public void tempUp_WithNoChange_BuildsResponseWithMessage() {
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_UP.asPiString())
				.put("result", NO_CHANGE.asPiString());

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("Don't you think it's hot enough already?");
	}

	@Test
	public void tempUp_WithFailure_BuildsResponseWithMessage() {
		ObjectNode responseFromPi = JsonNodeFactory.instance.objectNode()
				.put("command", HOUSE_TEMP_UP.asPiString())
				.put("result", FAILURE.asPiString())
				.put("failure-reason", "other reason");

		EchoResponse echoResponse = new TempAdjustmentResponseBuilder().buildResponse(responseFromPi);

		assertThat(echoResponse.getResponse().getOutputSpeech().getText()).isEqualTo("Your house could not turn the temp up. other reason");
	}

}