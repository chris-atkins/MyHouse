package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.thermostat.ThermostatStatus;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DateTime.class})
public class HouseStatusReporterTest {

	private HouseStatusReporter houseStatusReporter;

	private HouseStatusRepository houseStatusRepository;

	private DateTime currentTime = new DateTime();
	private DateTime oneDayAgo = currentTime.minusDays(1);

	@Before
	public void setup() {
		PowerMockito.mockStatic(DateTime.class);
		PowerMockito.when(DateTime.now()).thenReturn(currentTime);
		houseStatusRepository = Mockito.mock(HouseStatusRepository.class);
		houseStatusReporter = new HouseStatusReporter(houseStatusRepository);
	}

	@Test
	public void returnsAReportForLast24Hours() {
		List<HouseDataPoint> repositoryResponse = new ArrayList<>();

		DateTime time1 = new DateTime("2017-12-01T11:35:01");
		ThermostatStatus thermostatStatus1 = new ThermostatStatus(1.2, 3.4, null);
		repositoryResponse.add(new HouseDataPoint(time1, null, thermostatStatus1, null));

		DateTime time2 = new DateTime("2017-12-02T21:35:02");
		ThermostatStatus thermostatStatus2 = new ThermostatStatus(5.6, 7.8, null);
		repositoryResponse.add(new HouseDataPoint(time2, null, thermostatStatus2, null));

		DateTime time3 = new DateTime("2017-12-03T11:35:03");
		ThermostatStatus thermostatStatus3 = new ThermostatStatus(9.10, 11.12, null);
		repositoryResponse.add(new HouseDataPoint(time3, null, thermostatStatus3, null));

		when(houseStatusRepository.retrieveHouseStatusFrom(oneDayAgo, currentTime)).thenReturn(repositoryResponse);

		HouseStatusReport houseStatusReport = houseStatusReporter.reportForLast24Hours();
		assertThat(houseStatusReport.getLocalTimes()).containsExactly("12-01 11:35 AM", "12-02 09:35 PM", "12-03 11:35 AM");
		assertThat(houseStatusReport.getHouseTemperatures()).containsExactly(1.2, 5.6, 9.10);
		assertThat(houseStatusReport.getThermostatSettings()).containsExactly(3.4, 7.8, 11.12);
	}

	@Test
	public void passesExceptionFromRepository() {
		RuntimeException thrownException = new RuntimeException("something went wrong");
		when(houseStatusRepository.retrieveHouseStatusFrom(oneDayAgo, currentTime)).thenThrow(thrownException);

		try {
			houseStatusReporter.reportForLast24Hours();
			fail("expecting exception");
		} catch (Exception e) {
			assertThat(e).isEqualTo(thrownException);
		}
	}
}