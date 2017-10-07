package com.poorknight.server;

import com.poorknight.timedlights.OutsideLightsController;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FixedScheduleTaskManager {

	private ScheduledThreadPoolExecutor executor;
	private OutsideLightControllerRunnable controller;

	public FixedScheduleTaskManager(final ScheduledThreadPoolExecutor executor, final OutsideLightControllerRunnable controller) {
		this.executor = executor;
		this.controller = controller;
	}

	public void startAllTasks() {
		System.out.println("STARTING TASKS");
		executor.scheduleAtFixedRate(controller, 1, 5, TimeUnit.MINUTES);
	}

	public void stopAllTasks() {
		executor.shutdown();
	}

	static class OutsideLightControllerRunnable implements Runnable {

		private OutsideLightsController controller;

		OutsideLightControllerRunnable(final OutsideLightsController controller) {
			this.controller = controller;
		}

		@Override
		public void run() {
			controller.putLightsToCorrectStateForTimeOfDay();
		}
	}
}
