package com.poorknight.scheduledtasks;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.housestatus.HouseStatusRecorder;
import com.poorknight.scheduledtasks.FixedScheduleTaskManager.HouseStatusRecorderRunnable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HouseStatusRecorderRunnableTest {

	@Mock
	private HouseStatusRecorder houseStatusRecorder;

	@Mock
	private TextMessageAlerter textMessageAlerter;

	@Test
	public void runDelegatesCorrectly() {
		final HouseStatusRecorderRunnable task = new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);

		task.run();

		verify(houseStatusRecorder).recordCurrentHouseStatus();
	}

	@Test
	public void doesNotThrowException()  {
		final HouseStatusRecorderRunnable task = new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);

		Mockito.doThrow(new RuntimeException()).when(houseStatusRecorder).recordCurrentHouseStatus();

		task.run();
	}

	@Test
	public void sendsATextOnException()  {
		final HouseStatusRecorderRunnable task = new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);

		Mockito.doThrow(new RuntimeException()).when(houseStatusRecorder).recordCurrentHouseStatus();

		task.run();

		verify(textMessageAlerter).sendTextMessage("An exception occurred while recording house status.  Check the logs for details.");
	}

	@Test
	public void doesNotThrowAnExceptionIfOneHappensWhileSendingATextOnException()  {
		final HouseStatusRecorderRunnable task = new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);

		Mockito.doThrow(new RuntimeException()).when(houseStatusRecorder).recordCurrentHouseStatus();
		Mockito.doThrow(new RuntimeException()).when(textMessageAlerter).sendTextMessage(anyString());

		task.run();

		verify(textMessageAlerter).sendTextMessage("An exception occurred while recording house status.  Check the logs for details.");
	}
}