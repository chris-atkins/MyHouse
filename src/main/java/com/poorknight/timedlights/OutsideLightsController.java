package com.poorknight.timedlights;

import com.poorknight.echo.housecommand.HouseCommandMessager;

import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_OFF;
import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_ON;
import static com.poorknight.timedlights.DesiredState.OFF;
import static com.poorknight.timedlights.DesiredState.ON;

public class OutsideLightsController {

	private OutsideLightDesiredStateDecider decider;
	private HouseCommandMessager houseCommandMessager;

	public OutsideLightsController(final OutsideLightDesiredStateDecider decider, final HouseCommandMessager houseCommandMessager) {
		this.decider = decider;
		this.houseCommandMessager = houseCommandMessager;
	}

	public void putLightsToCorrectStateForTimeOfDay() {

		final DesiredState desiredState = decider.findDesiredState();

		if (desiredState == OFF) {
			houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_OFF);
		}

		if (desiredState == ON) {
			houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_ON);
		}
	}
}
