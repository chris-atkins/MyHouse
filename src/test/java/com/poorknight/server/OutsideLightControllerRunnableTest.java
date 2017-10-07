package com.poorknight.server;

import com.poorknight.server.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import com.poorknight.timedlights.OutsideLightsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OutsideLightControllerRunnableTest {

	@Mock
	private OutsideLightsController outsideLightsController;

	@Test
	public void runDelegatesCorrectly() throws Exception {
		final OutsideLightControllerRunnable task = new OutsideLightControllerRunnable(outsideLightsController);

		task.run();

		verify(outsideLightsController).putLightsToCorrectStateForTimeOfDay();
	}
}