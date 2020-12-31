package com.poorknight.scheduledtasks;

import com.poorknight.scheduledtasks.FixedScheduleTaskManager;
import com.poorknight.scheduledtasks.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.poorknight.scheduledtasks.FixedScheduleTaskManager.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FixedScheduleTaskManagerTest {

	@InjectMocks
	private FixedScheduleTaskManager fixedScheduleTaskManager;

	@Mock
	private OutsideLightControllerRunnable outsideLightsController;

	@Mock
	private AutomatedHouseTemperatureControllerRunnable automatedHouseTemperatureController;

	@Mock
	private HouseStatusRecorderRunnable houseStatusReportController;

	@Mock
	private ScheduledThreadPoolExecutor executor;

	@Test
	public void startCallsOutsideLightController() throws Exception {
		fixedScheduleTaskManager.startAllTasks();
		verify(executor, times(0)).scheduleAtFixedRate(outsideLightsController, 1L, 5L, TimeUnit.MINUTES);
	}

	@Test
	public void startCallsAutomatedTemperatureController() throws Exception {
		fixedScheduleTaskManager.startAllTasks();
		verify(executor, times(0)).scheduleAtFixedRate(automatedHouseTemperatureController, 2L, 10L, TimeUnit.MINUTES);
	}

	@Test
	public void startCallsHouseStatusReportController() throws Exception {
		fixedScheduleTaskManager.startAllTasks();
		verify(executor, times(0)).scheduleAtFixedRate(houseStatusReportController, 30L, 60L, TimeUnit.SECONDS);
	}

	@Test
	public void stopCallsExecutorShutdown() throws Exception {
		fixedScheduleTaskManager.stopAllTasks();
		verify(executor).shutdown();
	}
}