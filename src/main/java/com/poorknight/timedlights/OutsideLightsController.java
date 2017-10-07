package com.poorknight.timedlights;

import com.poorknight.echo.housecommand.HouseCommandMessager;

import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_OFF;
import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_ON;
import static com.poorknight.timedlights.DesiredState.OFF;
import static com.poorknight.timedlights.DesiredState.ON;

public class OutsideLightsController {

	private OutsideLightDesiredStateDecider decider;
	private HouseCommandMessager houseCommandMessager;

	public OutsideLightsController(final OutsideLightDesiredStateDecider decider,
								   final HouseCommandMessager houseCommandMessager) {
		this.decider = decider;
		this.houseCommandMessager = houseCommandMessager;
	}

	public void putLightsToCorrectStateForTimeOfDay() {

		if (decider.findDesiredState() == OFF) {
			houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_OFF);
		}

		if (decider.findDesiredState() == ON) {
			houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_ON);
		}
	}
}
