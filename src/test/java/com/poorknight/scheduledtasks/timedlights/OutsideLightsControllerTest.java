package com.poorknight.scheduledtasks.timedlights;

import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.poorknight.house.commands.HouseCommand.OUTSIDE_LIGHTS_OFF;
import static com.poorknight.house.commands.HouseCommand.OUTSIDE_LIGHTS_ON;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OutsideLightsControllerTest {

	@InjectMocks
	private OutsideLightsController outsideLightsController;

	@Mock
	private OutsideLightsDesiredStateDecider decider;

	@Mock
	private HouseCommandMessager houseCommandMessager;

	@Test
	public void delegatesCorrectly_ForOffState() throws Exception {
		Mockito.when(decider.findDesiredState()).thenReturn(DesiredState.OFF);

		outsideLightsController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(OUTSIDE_LIGHTS_OFF);
	}

	@Test
	public void delegatesCorrectly_ForOnState() throws Exception {
		Mockito.when(decider.findDesiredState()).thenReturn(DesiredState.ON);

		outsideLightsController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(OUTSIDE_LIGHTS_ON);
	}
}