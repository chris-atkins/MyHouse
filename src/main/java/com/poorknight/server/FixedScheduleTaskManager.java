package com.poorknight.server;

import com.poorknight.timedlights.OutsideLightsController;
import com.poorknight.timedtemp.AutomatedHouseTemperatureController;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FixedScheduleTaskManager {

	private ScheduledThreadPoolExecutor executor;
	private OutsideLightControllerRunnable outsideLigtscontroller;
	private AutomatedHouseTemperatureControllerRunnable automatedTempController;

	public FixedScheduleTaskManager(final ScheduledThreadPoolExecutor executor, final OutsideLightControllerRunnable outsideLigtscontroller, AutomatedHouseTemperatureControllerRunnable automatedTempController) {
		this.executor = executor;
		this.outsideLigtscontroller = outsideLigtscontroller;
		this.automatedTempController = automatedTempController;
	}

	public void startAllTasks() {
		System.out.println("STARTING TASKS");
		executor.scheduleAtFixedRate(outsideLigtscontroller, 1, 5, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(automatedTempController, 2L, 10L, TimeUnit.MINUTES);
		System.out.println("TASKS STARTED");
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
			try {
				controller.putLightsToCorrectStateForTimeOfDay();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	}

	static class AutomatedHouseTemperatureControllerRunnable implements Runnable {

		private AutomatedHouseTemperatureController automatedHouseTemperatureController;

		public AutomatedHouseTemperatureControllerRunnable(AutomatedHouseTemperatureController automatedHouseTemperatureController) {
			this.automatedHouseTemperatureController = automatedHouseTemperatureController;
		}

		@Override
		public void run() {
			try {
				this.automatedHouseTemperatureController.setTempAtTimeTriggers();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
