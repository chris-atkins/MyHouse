package com.poorknight.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.housestatus.reports.HouseDailySummary;
import com.poorknight.housestatus.reports.HouseDailySummaryReporter;
import com.poorknight.housestatus.reports.HouseStatusReport;
import com.poorknight.housestatus.reports.HouseStatusReporter;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportsEndpointTest {

	@InjectMocks
	private ReportsEndpoint reportsEndpoint;

	@Mock
	private HouseStatusReporter houseStatusReporter;

	@Mock
	private HouseDailySummaryReporter houseDailySummaryReporter;

	@Test
	public void reportLast24HoursDelegatesCorrectly() throws Exception {
		List<String> localTimes = Arrays.asList("hi", "there");
		List<Double> houseTemperatures = Arrays.asList(1.2, 3.4);
		List<Double> thermostatSettings = Arrays.asList(5.6, 7.8);
		HouseStatusReport report = new HouseStatusReport(localTimes, houseTemperatures, thermostatSettings);
		when(houseStatusReporter.reportForLast24Hours()).thenReturn(report);

		String results = reportsEndpoint.reportLast24Hours();
		HouseStatusReport houseStatusReport = new ObjectMapper().readValue(results, HouseStatusReport.class);

		assertThat(houseStatusReport).isEqualTo(report);
	}

	@Test
	public void reportsDailySummaries() throws Exception {
		LocalDate date = LocalDate.parse("2019-02-06");
		Integer numberOfMinutesHeaterIsOn = 2;
		Double averageHouseTemperature = 3d;
		Double averageExternalTemperature = 4d;
		Double averageInternalExternalTemperatureDifference = 5d;
		Double averageWindSpeed = 6d;
		Integer averateTimeBetweenHeaterCyclesAtOneTemp = 7;
		HouseDailySummary expectedSummary = new HouseDailySummary(numberOfMinutesHeaterIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageWindSpeed, averateTimeBetweenHeaterCyclesAtOneTemp);
		when(houseDailySummaryReporter.summaryForDay(date)).thenReturn(expectedSummary);

		String results = reportsEndpoint.singleDaySummary("2019-02-06");
		HouseDailySummary houseDailySummary = new ObjectMapper().readValue(results, HouseDailySummary.class);

		assertThat(houseDailySummary).isEqualTo(expectedSummary);
	}
}