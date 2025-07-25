package com.poorknight.scheduledtasks.timedlights;

import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.poorknight.house.commands.HouseCommand.PLANT_LIGHTS_OFF;
import static com.poorknight.house.commands.HouseCommand.PLANT_LIGHTS_ON;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlantLightsControllerTest {

	@InjectMocks
	private PlantLightsController plantLightsController;

	@Mock
	private PlantLightsDesiredStateDecider decider;

	@Mock
	private HouseCommandMessager houseCommandMessager;


	@Test
	public void callsCommandMessagerWithCorrectCommandIfDeciderSaysOn() {
		when(decider.findDesiredState()).thenReturn(ON);
		plantLightsController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(PLANT_LIGHTS_ON);
	}

	@Test
	public void callsCommandMessagerWithCorrectCommandIfDeciderSaysOff() {
		when(decider.findDesiredState()).thenReturn(OFF);
		plantLightsController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(PLANT_LIGHTS_OFF);
	}
}