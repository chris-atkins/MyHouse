package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.thermostat.ThermostatStatus;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HouseDailySummaryReporterTest {

	@InjectMocks
	private HouseDailySummaryReporter houseDailySummaryReporter;

	@Mock
	private HouseStatusRepository houseStatusRepository;

	@Test
	public void calculatesAverageTemp() {
		HouseDataPoint dp1 = buildDataPointWithTemp(50d);
		HouseDataPoint dp2 = buildDataPointWithTemp(61d);
		HouseDataPoint dp3 = buildDataPointWithTemp(-2d);
		HouseDataPoint dp4 = buildDataPointWithTemp(113d);
		HouseDataPoint dp5 = buildDataPointWithTemp(55.25d);
		HouseDataPoint dp6 = buildDataPointWithTemp(55.75d);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageHouseTemperature()).isEqualTo(55.5);
	}

	@Test
	public void calculatesNumberOfMinutesHeaterIsOn() {

	}

	private HouseDataPoint buildDataPointWithTemp(Double temp) {
		WeatherStatus weatherStatus = new WeatherStatus(null, null, null, null);
		ThermostatStatus thermostatStatus = new ThermostatStatus(temp, 0.0, null);
		return new HouseDataPoint(DateTime.now(), null, thermostatStatus, weatherStatus);
	}
}