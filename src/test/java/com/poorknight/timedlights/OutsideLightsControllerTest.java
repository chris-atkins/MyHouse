package com.poorknight.timedlights;

import com.poorknight.echo.housecommand.HouseCommandMessager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_OFF;
import static com.poorknight.echo.housecommand.HouseCommand.OUTSIDE_LIGHTS_ON;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OutsideLightsControllerTest {

	@InjectMocks
	private OutsideLightsController outsideLightsController;

	@Mock
	private OutsideLightDesiredStateDecider decider;

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