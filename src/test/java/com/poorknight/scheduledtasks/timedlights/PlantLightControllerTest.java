package com.poorknight.scheduledtasks.timedlights;

import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.poorknight.house.commands.HouseCommand.PLANT_LIGHTS_OFF;
import static com.poorknight.house.commands.HouseCommand.PLANT_LIGHTS_ON;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlantLightControllerTest {

	@InjectMocks
	private PlantLightController plantLightController;

	@Mock
	private PlantLightsDesiredStateDecider decider;

	@Mock
	private HouseCommandMessager houseCommandMessager;


	@Test
	public void callsCommandMessagerWithCorrectCommandIfDeciderSaysOn() {
		when(decider.findDesiredState()).thenReturn(ON);
		plantLightController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(PLANT_LIGHTS_ON);
	}

	@Test
	public void callsCommandMessagerWithCorrectCommandIfDeciderSaysOff() {
		when(decider.findDesiredState()).thenReturn(OFF);
		plantLightController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(PLANT_LIGHTS_OFF);
	}
}