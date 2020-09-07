package com.poorknight.scheduledtasks.timedlights;

import com.poorknight.house.commands.HouseCommandMessager;

import static com.poorknight.house.commands.HouseCommand.OUTSIDE_LIGHTS_OFF;
import static com.poorknight.house.commands.HouseCommand.OUTSIDE_LIGHTS_ON;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;

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
