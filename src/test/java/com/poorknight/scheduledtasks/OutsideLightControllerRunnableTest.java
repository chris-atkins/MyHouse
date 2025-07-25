package com.poorknight.scheduledtasks;

import com.poorknight.scheduledtasks.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import com.poorknight.scheduledtasks.timedlights.OutsideLightsController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OutsideLightControllerRunnableTest {

	@Mock
	private OutsideLightsController outsideLightsController;

	@Test
	public void runDelegatesCorrectly() throws Exception {
		final OutsideLightControllerRunnable task = new OutsideLightControllerRunnable(outsideLightsController);

		task.run();

		verify(outsideLightsController).putLightsToCorrectStateForTimeOfDay();
	}

	@Test
	public void doesNotThrowException() throws Exception {
		final OutsideLightControllerRunnable task = new OutsideLightControllerRunnable(outsideLightsController);

		Mockito.doThrow(new RuntimeException()).when(outsideLightsController).putLightsToCorrectStateForTimeOfDay();

		task.run();
	}
}