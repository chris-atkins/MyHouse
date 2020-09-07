package com.poorknight.scheduledtasks;

import com.poorknight.scheduledtasks.timedtemp.AutomatedHouseTemperatureController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.poorknight.scheduledtasks.FixedScheduleTaskManager.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AutomatedHouseTemperatureControllerRunnableTest {

	@Mock
	private AutomatedHouseTemperatureController automatedHouseTemperatureController;

	@Test
	public void runDelegatesCorrectly() {
		final AutomatedHouseTemperatureControllerRunnable task = new AutomatedHouseTemperatureControllerRunnable(automatedHouseTemperatureController);

		task.run();

		verify(automatedHouseTemperatureController).setTempAtTimeTriggers();
	}

	@Test(expected = Test.None.class)
	public void doesNotThrowException() throws Exception {
		final AutomatedHouseTemperatureControllerRunnable task = new AutomatedHouseTemperatureControllerRunnable(automatedHouseTemperatureController);

		Mockito.doThrow(new RuntimeException()).when(automatedHouseTemperatureController).setTempAtTimeTriggers();

		task.run();
	}
}