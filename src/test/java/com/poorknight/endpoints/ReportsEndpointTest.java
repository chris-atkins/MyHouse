package com.poorknight.endpoints;

import com.poorknight.alerting.textmessage.TextMessageAlerter;
import com.poorknight.endpoints.houseip.HouseIpRequest;
import com.poorknight.endpoints.houseip.HouseIpResponse;
import com.poorknight.endpoints.notification.NotifyRequest;
import com.poorknight.endpoints.notification.NotifyResponse;
import com.poorknight.housestatus.reports.HouseStatusReport;
import com.poorknight.housestatus.reports.HouseStatusReporter;
import com.poorknight.server.WebResourceFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportsEndpointTest {

	private final NotifyRequest notifyRequest = new NotifyRequest(RandomStringUtils.random(50));

	@InjectMocks
	private ReportsEndpoint reportsEndpoint;

	@Mock
	private HouseStatusReporter houseStatusReporter;

	@Test
	public void reportLast24HoursDelegatesCorrectly() throws Exception {
		List<String> localTimes = Arrays.asList("hi", "there");
		List<Double> houseTemperatures = Arrays.asList(1.2, 3.4);
		List<Double> thermostatSettings = Arrays.asList(5.6, 7.8);
		HouseStatusReport report = new HouseStatusReport(localTimes, houseTemperatures, thermostatSettings);
		when(houseStatusReporter.reportForLast24Hours()).thenReturn(report);

		HouseStatusReport houseStatusReport = reportsEndpoint.reportLast24Hours();

		assertThat(houseStatusReport).isEqualTo(report);
	}
}