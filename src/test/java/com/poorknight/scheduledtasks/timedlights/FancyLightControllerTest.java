package com.poorknight.scheduledtasks.timedlights;

import com.poorknight.house.commands.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.poorknight.house.commands.HouseCommand.*;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.OFF;
import static com.poorknight.scheduledtasks.timedlights.DesiredState.ON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FancyLightControllerTest {

	@InjectMocks
	private FancyLightController fancyLightsController;

	@Mock
	private FancyLightDesiredStateDecider decider;

	@Mock
	private HouseCommandMessager houseCommandMessager;


	@Test
	public void callsCommandMessagerWithCorrectCommandIfDeciderSaysOn() {
		when(decider.findDesiredState()).thenReturn(ON);
		fancyLightsController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(FANCY_LIGHT_ON);
	}

	@Test
	public void callsCommandMessagerWithCorrectCommandIfDeciderSaysOff() {
		when(decider.findDesiredState()).thenReturn(OFF);
		fancyLightsController.putLightsToCorrectStateForTimeOfDay();

		verify(houseCommandMessager).requestHouseCommand(FANCY_LIGHT_OFF);
	}
}