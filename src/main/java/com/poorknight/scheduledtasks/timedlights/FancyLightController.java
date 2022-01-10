package com.poorknight.scheduledtasks.timedlights;

import com.poorknight.house.commands.HouseCommandMessager;

import static com.poorknight.house.commands.HouseCommand.*;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;

public class FancyLightController {

	private FancyLightDesiredStateDecider decider;
	private HouseCommandMessager houseCommandMessager;

	public FancyLightController(final FancyLightDesiredStateDecider decider, final HouseCommandMessager houseCommandMessager) {
		this.decider = decider;
		this.houseCommandMessager = houseCommandMessager;
	}

	public void putLightsToCorrectStateForTimeOfDay() {
		final DesiredState desiredState = decider.findDesiredState();

		if (desiredState == OFF) {
			houseCommandMessager.requestHouseCommand(FANCY_LIGHT_OFF);
		}

		if (desiredState == ON) {
			houseCommandMessager.requestHouseCommand(FANCY_LIGHT_ON);
		}
	}
}
