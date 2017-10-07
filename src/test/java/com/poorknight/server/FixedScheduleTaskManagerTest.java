package com.poorknight.server;

import com.poorknight.server.FixedScheduleTaskManager.OutsideLightControllerRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FixedScheduleTaskManagerTest {

	@InjectMocks
	private FixedScheduleTaskManager fixedScheduleTaskManager;

	@Mock
	private OutsideLightControllerRunnable outsideLightsController;

	@Mock
	private ScheduledThreadPoolExecutor executor;

	@Test
	public void startCallsOutsideLightController() throws Exception {
		fixedScheduleTaskManager.startAllTasks();
		verify(executor).scheduleAtFixedRate(outsideLightsController, 5L, 5L, TimeUnit.MINUTES);
	}

	@Test
	public void stopCallsExecutorShutdown() throws Exception {
		fixedScheduleTaskManager.stopAllTasks();
		verify(executor).shutdown();
	}
}