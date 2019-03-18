package com.poorknight.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.poorknight.endpoints.houseip.HouseIpRequest;
import com.poorknight.endpoints.houseip.HouseIpResponse;
import com.poorknight.server.WebResourceFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.endpoints.notification.NotifyRequest;
import com.poorknight.endpoints.notification.NotifyResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TextMessageAlerter.class, WebResourceFactory.class})
public class HouseEndpointTest {

	private final NotifyRequest notifyRequest = new NotifyRequest(RandomStringUtils.random(50));

	@InjectMocks
	private HouseEndpoint houseEndpoint;

	@Mock
	private TextMessageAlerter textMessageAlerter;

	@Before
	public void setup() {
		PowerMockito.mockStatic(TextMessageAlerter.class);
		PowerMockito.mockStatic(WebResourceFactory.class);
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

	@Test
	public void updateHouseIp_UpdatesTheHouseIp() {
		HouseIpRequest houseIpRequest = new HouseIpRequest("1.2.3.4");
		houseEndpoint.updateHouseUrl(houseIpRequest);

		PowerMockito.verifyStatic(WebResourceFactory.class);
		WebResourceFactory.setHouseIp(eq("1.2.3.4"));
	}

	@Test
	public void updateHouseIp_ReturnsSuccessMessage() {
		HouseIpRequest houseIpRequest = new HouseIpRequest("1.2.3.4");
		HouseIpResponse response = houseEndpoint.updateHouseUrl(houseIpRequest);

		Assertions.assertThat(response.getMessage()).isEqualTo("Successfully updated house ip to 1.2.3.4");
	}
}
