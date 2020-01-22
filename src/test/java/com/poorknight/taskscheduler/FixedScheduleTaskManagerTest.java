package com.poorknight.taskscheduler;

import com.poorknight.taskscheduler.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.poorknight.taskscheduler.FixedScheduleTaskManager.*;
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
		verify(executor).scheduleAtFixedRate(outsideLightsController, 1L, 5L, TimeUnit.MINUTES);
	}

	@Test
	public void startCallsAutomatedTemperatureController() throws Exception {
		fixedScheduleTaskManager.startAllTasks();
		verify(executor).scheduleAtFixedRate(automatedHouseTemperatureController, 2L, 10L, TimeUnit.MINUTES);
	}

	@Test
	public void startCallsHouseStatusReportController() throws Exception {
		fixedScheduleTaskManager.startAllTasks();
		verify(executor).scheduleAtFixedRate(houseStatusReportController, 30L, 60L, TimeUnit.SECONDS);
	}

	@Test
	public void stopCallsExecutorShutdown() throws Exception {
		fixedScheduleTaskManager.stopAllTasks();
		verify(executor).shutdown();
	}
}