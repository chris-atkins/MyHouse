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
		System.out.println("STARTING TO SET LIGHTS TO CORRECT STATE");

		final DesiredState desiredState = decider.findDesiredState();

		if (desiredState == OFF) {
			System.out.println("TURNING OUTSIDE LIGHTS OFF");
			houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_OFF);
		}

		if (desiredState == ON) {
			System.out.println("TURNING OUTSIDE LIGHTS ON");
			houseCommandMessager.requestHouseCommand(OUTSIDE_LIGHTS_ON);
		}
		System.out.println("DONE SETTING LIGHTS TO CORRECT STATE");
	}
}
