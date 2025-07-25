package com.poorknight.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;

import com.poorknight.endpoints.houseip.HouseIpRequest;
import com.poorknight.endpoints.houseip.HouseIpResponse;
import com.poorknight.server.WebResourceFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.endpoints.notification.NotifyRequest;
import com.poorknight.endpoints.notification.NotifyResponse;

@ExtendWith(MockitoExtension.class)
public class HouseEndpointTest {

	private final NotifyRequest notifyRequest = new NotifyRequest(RandomStringUtils.random(50));

	@InjectMocks
	private HouseEndpoint houseEndpoint;

	@Mock
	private TextMessageAlerter textMessageAlerter;

	@Test
	public void notifyMe_SendsMessageWithCorrectText() throws Exception {
		try(MockedStatic<TextMessageAlerter> staticAlerter = Mockito.mockStatic(TextMessageAlerter.class)) {
			staticAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

			final NotifyRequest notifyRequest = new NotifyRequest("Message Content");
			houseEndpoint.notifyMe(notifyRequest);

			verify(textMessageAlerter).sendTextMessage("Message Content");
		}
	}

	@Test
	public void notifyMe_returnsSuccessMessage() throws Exception {
		try(MockedStatic<TextMessageAlerter> staticAlerter = Mockito.mockStatic(TextMessageAlerter.class)) {
			staticAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

			final NotifyResponse response = houseEndpoint.notifyMe(notifyRequest);
			assertThat(response.getMessage(), equalTo("Message sent."));
		}
	}

	@Test
	public void updateHouseIp_UpdatesTheHouseIp() {
		try(MockedStatic<TextMessageAlerter> staticAlerter = Mockito.mockStatic(TextMessageAlerter.class)) {
			try(MockedStatic<WebResourceFactory> mockedWebResourceFactory = Mockito.mockStatic(WebResourceFactory.class)) {

				staticAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

				HouseIpRequest houseIpRequest = new HouseIpRequest("1.2.3.4");
				houseEndpoint.updateHouseUrl(houseIpRequest);

				mockedWebResourceFactory.verify(() -> WebResourceFactory.setHouseIp("1.2.3.4"));
			}
		}
	}

	@Test
	public void updateHouseIp_ReturnsSuccessMessage() {
		try(MockedStatic<TextMessageAlerter> staticAlerter = Mockito.mockStatic(TextMessageAlerter.class)) {
			staticAlerter.when(TextMessageAlerter::instance).thenReturn(textMessageAlerter);

			HouseIpRequest houseIpRequest = new HouseIpRequest("1.2.3.4");
			HouseIpResponse response = houseEndpoint.updateHouseUrl(houseIpRequest);

			Assertions.assertThat(response.getMessage()).isEqualTo("Successfully updated house ip to 1.2.3.4");
		}
	}
}
