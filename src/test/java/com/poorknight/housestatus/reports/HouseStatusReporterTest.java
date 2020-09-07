package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.house.thermostat.ThermostatStatus;
import com.poorknight.time.TimeFinder;
import com.poorknight.time.TimeFinder.UtcTimeRange;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class HouseStatusReporterTest {

	private HouseStatusReporter houseStatusReporter;

	private HouseStatusRepository houseStatusRepository;

	@Before
	public void setup() {
		houseStatusRepository = Mockito.mock(HouseStatusRepository.class);
		houseStatusReporter = new HouseStatusReporter(houseStatusRepository);
	}

	@Test
	public void returnsAReportForRequestedDay() {
		List<HouseDataPoint> repositoryResponse = new ArrayList<>();

		DateTime time1 = new DateTime("2017-12-01T11:35:01");
		ThermostatStatus thermostatStatus1 = new ThermostatStatus(1.2, 3.4, null, null);
		repositoryResponse.add(new HouseDataPoint(time1, null, thermostatStatus1, null));

		DateTime time2 = new DateTime("2017-12-02T21:35:02");
		ThermostatStatus thermostatStatus2 = new ThermostatStatus(5.6, 7.8, null, null);
		repositoryResponse.add(new HouseDataPoint(time2, null, thermostatStatus2, null));

		DateTime time3 = new DateTime("2017-12-03T11:35:03");
		ThermostatStatus thermostatStatus3 = new ThermostatStatus(9.10, 11.12, null, null);
		repositoryResponse.add(new HouseDataPoint(time3, null, thermostatStatus3, null));

		LocalDate date = new LocalDate();
		UtcTimeRange dateRange = new TimeFinder().getUtcRangeForLocalDay(date);
		when(houseStatusRepository.retrieveHouseStatusFrom(dateRange.getStartTime(), dateRange.getEndTime())).thenReturn(repositoryResponse);

		HouseStatusReport houseStatusReport = houseStatusReporter.reportForDay(date);
		assertThat(houseStatusReport.getLocalTimes()).containsExactly("12-01 11:35 AM", "12-02 09:35 PM", "12-03 11:35 AM");
		assertThat(houseStatusReport.getHouseTemperatures()).containsExactly(1.2, 5.6, 9.10);
		assertThat(houseStatusReport.getThermostatSettings()).containsExactly(3.4, 7.8, 11.12);
	}

	@Test
	public void ignoresDataPointsThatAreTheSameTempAndSettingAsTheLast() {
		List<HouseDataPoint> repositoryResponse = new ArrayList<>();

		DateTime time1 = new DateTime("2017-12-01T11:35:01");
		ThermostatStatus thermostatStatus1 = new ThermostatStatus(1.2, 3.4, null, null);
		repositoryResponse.add(new HouseDataPoint(time1, null, thermostatStatus1, null));

		DateTime time2 = new DateTime("2017-12-02T21:35:02");
		ThermostatStatus thermostatStatus2 = new ThermostatStatus(5.6, 7.8, null, null);
		repositoryResponse.add(new HouseDataPoint(time2, null, thermostatStatus2, null));

		DateTime time3 = new DateTime("2017-12-02T22:35:02");
		ThermostatStatus thermostatStatus3 = new ThermostatStatus(5.6, 7.8, null, null);
		repositoryResponse.add(new HouseDataPoint(time3, null, thermostatStatus3, null));

		DateTime time4 = new DateTime("2017-12-02T23:35:02");
		ThermostatStatus thermostatStatus4 = new ThermostatStatus(5.6, 7.8, null, null);
		repositoryResponse.add(new HouseDataPoint(time4, null, thermostatStatus4, null));

		DateTime time5 = new DateTime("2017-12-03T11:35:03");
		ThermostatStatus thermostatStatus5 = new ThermostatStatus(9.10, 11.12, null, null);
		repositoryResponse.add(new HouseDataPoint(time5, null, thermostatStatus5, null));

		LocalDate date = new LocalDate();
		UtcTimeRange dateRange = new TimeFinder().getUtcRangeForLocalDay(date);
		when(houseStatusRepository.retrieveHouseStatusFrom(dateRange.getStartTime(), dateRange.getEndTime())).thenReturn(repositoryResponse);

		HouseStatusReport houseStatusReport = houseStatusReporter.reportForDay(date);
		assertThat(houseStatusReport.getLocalTimes()).containsExactly("12-01 11:35 AM", "12-02 09:35 PM", "12-03 11:35 AM");
		assertThat(houseStatusReport.getHouseTemperatures()).containsExactly(1.2, 5.6, 9.10);
		assertThat(houseStatusReport.getThermostatSettings()).containsExactly(3.4, 7.8, 11.12);
	}

	@Test
	public void passesExceptionFromRepository() {
		LocalDate date = new LocalDate();
		UtcTimeRange dateRange = new TimeFinder().getUtcRangeForLocalDay(date);
		RuntimeException thrownException = new RuntimeException("something went wrong");
		when(houseStatusRepository.retrieveHouseStatusFrom(dateRange.getStartTime(), dateRange.getEndTime())).thenThrow(thrownException);

		try {
			houseStatusReporter.reportForDay(date);
			fail("expecting exception");
		} catch (Exception e) {
			assertThat(e).isEqualTo(thrownException);
		}
	}
}