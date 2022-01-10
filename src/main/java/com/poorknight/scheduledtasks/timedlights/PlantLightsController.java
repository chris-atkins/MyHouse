package com.poorknight.scheduledtasks.timedlights;

import com.poorknight.house.commands.HouseCommandMessager;

import static com.poorknight.house.commands.HouseCommand.*;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;

public class PlantLightsController {

	private PlantLightsDesiredStateDecider decider;
	private HouseCommandMessager houseCommandMessager;

	public PlantLightsController(final PlantLightsDesiredStateDecider decider, final HouseCommandMessager houseCommandMessager) {
		this.decider = decider;
		this.houseCommandMessager = houseCommandMessager;
	}

	public void putLightsToCorrectStateForTimeOfDay() {
		final DesiredState desiredState = decider.findDesiredState();

		if (desiredState == OFF) {
			houseCommandMessager.requestHouseCommand(PLANT_LIGHTS_OFF);
		}

		if (desiredState == ON) {
			houseCommandMessager.requestHouseCommand(PLANT_LIGHTS_ON);
		}
	}
}
