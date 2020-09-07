package com.poorknight.scheduledtasks;

import com.poorknight.scheduledtasks.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import com.poorknight.scheduledtasks.timedlights.OutsideLightsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

	@Test(expected = Test.None.class)
	public void doesNotThrowException() throws Exception {
		final OutsideLightControllerRunnable task = new OutsideLightControllerRunnable(outsideLightsController);

		Mockito.doThrow(new RuntimeException()).when(outsideLightsController).putLightsToCorrectStateForTimeOfDay();

		task.run();
	}
}