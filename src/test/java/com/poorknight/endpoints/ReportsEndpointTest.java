package com.poorknight.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.housestatus.reports.HouseDailySummary;
import com.poorknight.housestatus.reports.HouseDailySummaryReporter;
import com.poorknight.housestatus.reports.HouseStatusReport;
import com.poorknight.housestatus.reports.HouseStatusReporter;
import com.poorknight.time.TimeFinder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

	@Mock
	private TimeFinder timeFinder;

	@Test
	public void reportLast24HoursDelegatesCorrectly() throws Exception {
		List<String> localTimes = Arrays.asList("hi", "there");
		List<Double> houseTemperatures = Arrays.asList(1.2, 3.4);
		List<Double> thermostatSettings = Arrays.asList(5.6, 7.8);
		HouseStatusReport report = new HouseStatusReport(localTimes, houseTemperatures, thermostatSettings);

		when(timeFinder.getCurrentLocalTime()).thenReturn(DateTime.parse("2019-02-25T05:5:00.000Z"));
		LocalDate date = LocalDate.parse("2019-02-24");
		when(houseStatusReporter.reportForDay(date)).thenReturn(report);

		String results = reportsEndpoint.reportLastDay();
		HouseStatusReport houseStatusReport = new ObjectMapper().readValue(results, HouseStatusReport.class);

		assertThat(houseStatusReport).isEqualTo(report);
	}

	@Test
	public void reportsDailySummaries() throws Exception {
		Integer numberOfMinutesDataExistsFor = 7;
		Integer numberOfMinutesHeaterIsOn = 12;
		Integer numberOfMinutesACIsOn = 2;
		Double averageHouseTemperature = 3d;
		Double averageExternalTemperature = 4d;
		Double averageInternalExternalTemperatureDifference = 5d;
		Double averageWindSpeed = 6d;
		Double averateTimeBetweenHeaterCyclesAtOneTemp = 7d;
		Double averateTimeBetweenACCyclesAtOneTemp = 12.3;
		Double averageHouseTempSetting = 34d;
		HouseDailySummary expectedSummary = new HouseDailySummary(numberOfMinutesDataExistsFor, numberOfMinutesHeaterIsOn, numberOfMinutesACIsOn, averageHouseTemperature, averageExternalTemperature, averageInternalExternalTemperatureDifference, averageHouseTempSetting, averageWindSpeed, averateTimeBetweenHeaterCyclesAtOneTemp, averateTimeBetweenACCyclesAtOneTemp);
		when(timeFinder.getCurrentLocalTime()).thenReturn(DateTime.parse("2019-02-25T05:5:00.000Z"));
		LocalDate date = LocalDate.parse("2019-02-24");
		when(houseDailySummaryReporter.summaryForDay(date)).thenReturn(expectedSummary);

		String results = reportsEndpoint.singleDaySummary();
		HouseDailySummary houseDailySummary = new ObjectMapper().readValue(results, HouseDailySummary.class);

		assertThat(houseDailySummary).isEqualTo(expectedSummary);
	}
}