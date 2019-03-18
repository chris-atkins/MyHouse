package com.poorknight.server;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.housestatus.HouseStatusRecorder;
import com.poorknight.server.FixedScheduleTaskManager.HouseStatusRecorderRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
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

	@Test(expected = Test.None.class)
	public void doesNotThrowException()  {
		final HouseStatusRecorderRunnable task = new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);

		Mockito.doThrow(new RuntimeException()).when(houseStatusRecorder).recordCurrentHouseStatus();

		task.run();
	}

	@Test(expected = Test.None.class)
	public void sendsATextOnException()  {
		final HouseStatusRecorderRunnable task = new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);

		Mockito.doThrow(new RuntimeException()).when(houseStatusRecorder).recordCurrentHouseStatus();

		task.run();

		verify(textMessageAlerter).sendTextMessage("An exception occurred while recording house status.  Check the logs for details.");
	}

	@Test(expected = Test.None.class)
	public void doesNotThrowAnExceptionIfOneHappensWhileSendingATextOnException()  {
		final HouseStatusRecorderRunnable task = new HouseStatusRecorderRunnable(houseStatusRecorder, textMessageAlerter);

		Mockito.doThrow(new RuntimeException()).when(houseStatusRecorder).recordCurrentHouseStatus();
		Mockito.doThrow(new RuntimeException()).when(textMessageAlerter).sendTextMessage(anyString());

		task.run();

		verify(textMessageAlerter).sendTextMessage("An exception occurred while recording house status.  Check the logs for details.");
	}
}