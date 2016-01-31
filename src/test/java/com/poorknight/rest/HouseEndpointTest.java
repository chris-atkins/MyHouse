package com.poorknight.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.rest.notification.NotifyRequest;
import com.poorknight.rest.notification.NotifyResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextMessageAlerter.class)
public class HouseEndpointTest {

	private final NotifyRequest notifyRequest = new NotifyRequest(RandomStringUtils.random(50));

	@InjectMocks
	private HouseEndpoint houseEndpoint;

	@Mock
	private TextMessageAlerter textMessageAlerter;

	@Before
	public void setup() {
		PowerMockito.mockStatic(TextMessageAlerter.class);
		when(TextMessageAlerter.instance()).thenReturn(textMessageAlerter);
	}

	@Test
	public void notifyMe_SendsMessageWithCorrectText() throws Exception {
		final NotifyRequest notifyRequest = new NotifyRequest("Message Content");
		houseEndpoint.notifyMe(notifyRequest);

		verify(textMessageAlerter).sendTextMessage("Message Content");
	}

	@Test
	public void notifyMe_returnsSuccessMessage() throws Exception {
		final NotifyResponse response = houseEndpoint.notifyMe(notifyRequest);
		assertThat(response.getMessage(), equalTo("Message sent."));
	}
}
