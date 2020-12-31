package com.poorknight.scheduledtasks;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.housestatus.HouseStatusRecorder;
import com.poorknight.scheduledtasks.timedlights.OutsideLightsController;
import com.poorknight.scheduledtasks.timedtemp.AutomatedHouseTemperatureController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.joda.time.DateTime;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@SuppressWarnings("PMD")
@SuppressFBWarnings(value="URF_UNREAD_FIELD")
public class FixedScheduleTaskManager {

	private ScheduledThreadPoolExecutor executor;
	private OutsideLightControllerRunnable outsideLightsController;
	private AutomatedHouseTemperatureControllerRunnable automatedTempController;
	private HouseStatusRecorderRunnable houseStatusRecorder;


	public FixedScheduleTaskManager(final ScheduledThreadPoolExecutor executor, final OutsideLightControllerRunnable outsideLightsController, AutomatedHouseTemperatureControllerRunnable automatedTempController, HouseStatusRecorderRunnable houseStatusRecorder) {
		this.executor = executor;
		this.outsideLightsController = outsideLightsController;
		this.automatedTempController = automatedTempController;
		this.houseStatusRecorder = houseStatusRecorder;
	}

	public void startAllTasks() {
		System.out.println("STARTING TASKS");
//		executor.scheduleAtFixedRate(outsideLightsController, 1L, 5, TimeUnit.MINUTES);
//		executor.scheduleAtFixedRate(automatedTempController, 2L, 10L, TimeUnit.MINUTES);
//		executor.scheduleAtFixedRate(houseStatusRecorder, 30L, 60L, TimeUnit.SECONDS);
		System.out.println("TASKS STARTED");
	}

	public void stopAllTasks() {
		executor.shutdown();
	}

	public static class OutsideLightControllerRunnable implements Runnable {

		private OutsideLightsController controller;

		public OutsideLightControllerRunnable(final OutsideLightsController controller) {
			this.controller = controller;
		}

		@Override
		public void run() {
			try {
				System.out.println(new DateTime().toLocalDateTime().toString() + ": Initiating timed OutsideLightsController.putLightsToCorrectStateForTimeOfDay()");
				controller.putLightsToCorrectStateForTimeOfDay();
			} catch (RuntimeException e) {
				System.out.println("Problem running OutsideLightControllerRunnable");
				e.printStackTrace();
			}
		}
	}

	public static class AutomatedHouseTemperatureControllerRunnable implements Runnable {

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

	public static class HouseStatusRecorderRunnable implements Runnable {

		private final HouseStatusRecorder houseStatusRecorder;
		private final TextMessageAlerter textMessageAlerter;

		public HouseStatusRecorderRunnable(HouseStatusRecorder houseStatusRecorder, TextMessageAlerter textMessageAlerter) {
			this.houseStatusRecorder = houseStatusRecorder;
			this.textMessageAlerter = textMessageAlerter;
		}

		@Override
		public void run() {
			try {
				houseStatusRecorder.recordCurrentHouseStatus();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					textMessageAlerter.sendTextMessage("An exception occurred while recording house status.  Check the logs for details.");
				} catch (Exception textException) {
					textException.printStackTrace();
				}
			}
		}
	}
}
