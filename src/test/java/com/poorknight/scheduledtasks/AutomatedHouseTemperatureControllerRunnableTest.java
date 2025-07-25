package com.poorknight.scheduledtasks;

import com.poorknight.scheduledtasks.timedtemp.AutomatedHouseTemperatureController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.poorknight.scheduledtasks.FixedScheduleTaskManager.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AutomatedHouseTemperatureControllerRunnableTest {

	@Mock
	private AutomatedHouseTemperatureController automatedHouseTemperatureController;

	@Test
	public void runDelegatesCorrectly() {
		final AutomatedHouseTemperatureControllerRunnable task = new AutomatedHouseTemperatureControllerRunnable(automatedHouseTemperatureController);

		task.run();

		verify(automatedHouseTemperatureController).setTempAtTimeTriggers();
	}

	@Test
	public void doesNotThrowException() throws Exception {
		final AutomatedHouseTemperatureControllerRunnable task = new AutomatedHouseTemperatureControllerRunnable(automatedHouseTemperatureController);

		Mockito.doThrow(new RuntimeException()).when(automatedHouseTemperatureController).setTempAtTimeTriggers();

		task.run();
	}
}