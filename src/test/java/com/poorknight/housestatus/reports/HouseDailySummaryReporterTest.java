package com.poorknight.housestatus.reports;

import com.poorknight.housestatus.repository.HouseDataPoint;
import com.poorknight.housestatus.repository.HouseStatusRepository;
import com.poorknight.housestatus.weather.WeatherStatus;
import com.poorknight.thermostat.ThermostatStatus;
import com.poorknight.thermostat.ThermostatStatus.FurnaceState;
import com.poorknight.thermostat.ThermostatStatus.ThermostatMode;
import org.assertj.core.data.Offset;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HouseDailySummaryReporterTest {

	@InjectMocks
	private HouseDailySummaryReporter houseDailySummaryReporter;

	@Mock
	private HouseStatusRepository houseStatusRepository;


	DateTime startingTime;
	int minutesCounter;

	@Before
	public void setUp() {
		startingTime = DateTime.now();
		minutesCounter = 0;
	}

	@Test
	public void callsRepositoryForFullDayAroundPassedTime() {
		houseDailySummaryReporter.summaryForDay(LocalDate.parse("2018-02-25"));
		verify(houseStatusRepository).retrieveHouseStatusFrom(DateTime.parse("2018-02-25T05:00:00.000Z"), DateTime.parse("2018-02-26T04:59:59.999Z"));

		houseDailySummaryReporter.summaryForDay(LocalDate.parse("2018-06-25"));
		verify(houseStatusRepository).retrieveHouseStatusFrom(DateTime.parse("2018-06-25T04:00:00.000Z"), DateTime.parse("2018-06-26T03:59:59.999Z"));
	}

	@Test
	public void returnsNullsOnEmptyList() {
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Collections.emptyList());

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getNumberOfMinutesDataExistsFor()).isEqualTo(0);
		assertThat(houseDailySummary.getNumberOfMinutesHeaterIsOn()).isEqualTo(null);
		assertThat(houseDailySummary.getAverageWindSpeed()).isEqualTo(null);
		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(null);
		assertThat(houseDailySummary.getAverageInternalExternalTemperatureDifference()).isEqualTo(null);
		assertThat(houseDailySummary.getAverageExternalTemperature()).isEqualTo(null);
		assertThat(houseDailySummary.getAverageHouseTempSetting()).isEqualTo(null);
		assertThat(houseDailySummary.getAverageHouseTemperature()).isEqualTo(null);
	}

	@Test
	public void calculatesAverageTemp() {
		HouseDataPoint dp1 = buildDataPointWithHouseTemp(50d);
		HouseDataPoint dp2 = buildDataPointWithHouseTemp(61d);
		HouseDataPoint dp3 = buildDataPointWithHouseTemp(-2d);
		HouseDataPoint dp4 = buildDataPointWithHouseTemp(113d);
		HouseDataPoint dp5 = buildDataPointWithHouseTemp(55.25d);
		HouseDataPoint dp6 = buildDataPointWithHouseTemp(55.75d);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageHouseTemperature()).isEqualTo(55.5);
	}

	@Test
	public void calculatesNumberOfMinutesHeaterIsOn() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceState(FurnaceState.HEAT_ON);
		HouseDataPoint dp2 = buildDataPointWithFurnaceState(FurnaceState.OFF);
		HouseDataPoint dp3 = buildDataPointWithFurnaceState(FurnaceState.HEAT_ON);
		HouseDataPoint dp4 = buildDataPointWithFurnaceState(FurnaceState.HEAT_ON);
		HouseDataPoint dp5 = buildDataPointWithFurnaceState(FurnaceState.OFF);
		HouseDataPoint dp6 = buildDataPointWithFurnaceState(FurnaceState.HEAT_ON);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getNumberOfMinutesHeaterIsOn()).isEqualTo(4);
	}

	@Test
	public void calculatesNumberOfMinutesDataExistsFor() {
		HouseDataPoint dp1 = buildEmptyDataSet();
		HouseDataPoint dp2 = buildEmptyDataSet();
		HouseDataPoint dp3 = buildEmptyDataSet();
		HouseDataPoint dp4 = buildEmptyDataSet();
		HouseDataPoint dp5 = buildEmptyDataSet();
		HouseDataPoint dp6 = buildEmptyDataSet();
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getNumberOfMinutesDataExistsFor()).isEqualTo(6);
	}

	@Test
	public void calculatesAverageTempSetting() {
		HouseDataPoint dp1 = buildDataPointWitTempSetting(1.2);
		HouseDataPoint dp2 = buildDataPointWitTempSetting(2.3);
		HouseDataPoint dp3 = buildDataPointWitTempSetting(3.4);
		HouseDataPoint dp4 = buildDataPointWitTempSetting(5.6);
		HouseDataPoint dp5 = buildDataPointWitTempSetting(7.8);
		HouseDataPoint dp6 = buildDataPointWitTempSetting(9.4);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageHouseTempSetting()).isEqualTo(4.95);
	}

	@Test
	public void calculatesAverageExternalTemp() {
		HouseDataPoint dp1 = buildDataPointWitExternalTemp(11.2);
		HouseDataPoint dp2 = buildDataPointWitExternalTemp(12.3);
		HouseDataPoint dp3 = buildDataPointWitExternalTemp(13.4);
		HouseDataPoint dp4 = buildDataPointWitExternalTemp(15.6);
		HouseDataPoint dp5 = buildDataPointWitExternalTemp(17.8);
		HouseDataPoint dp6 = buildDataPointWitExternalTemp(19.4);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageExternalTemperature()).isCloseTo(14.95, Offset.offset(.00001));
	}

	@Test
	public void calculatesAverageInternalExternalTempDiff() {
		HouseDataPoint dp1 = buildDataPointWithInternalAndExternalTemp(1.2, 6.2);
		HouseDataPoint dp2 = buildDataPointWithInternalAndExternalTemp(2, 9);
		HouseDataPoint dp3 = buildDataPointWithInternalAndExternalTemp(3.4, 5.4);
		HouseDataPoint dp4 = buildDataPointWithInternalAndExternalTemp(5.6, 105.6);
		HouseDataPoint dp5 = buildDataPointWithInternalAndExternalTemp(7, 8);
		HouseDataPoint dp6 = buildDataPointWithInternalAndExternalTemp(9, -4);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageInternalExternalTemperatureDifference()).isEqualTo(17d);
	}

	@Test
	public void calculatesAverageWindSpeed() {
		HouseDataPoint dp1 = buildDataPointWithWindSpeed(1.2);
		HouseDataPoint dp2 = buildDataPointWithWindSpeed(2);
		HouseDataPoint dp3 = buildDataPointWithWindSpeed(3.4);
		HouseDataPoint dp4 = buildDataPointWithWindSpeed(5.6);
		HouseDataPoint dp5 = buildDataPointWithWindSpeed(7);
		HouseDataPoint dp6 = buildDataPointWithWindSpeed(9);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageWindSpeed()).isEqualTo(4.7);
	}

	@Test
	public void calculatesTimeBetweenTempsWithAlwaysTheSameTemp() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(1.5);
	}

	@Test
	public void calculatesTimeBetweenTemps_DoesNotCountInitialOFFStatus() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp7 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp8 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp9 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp10 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(1.5);
	}

	@Test
	public void calculatesTimeBetweenTemps_DoesNotCountTrailingOFFStatus() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp7 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 57.0);
		HouseDataPoint dp8 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp9 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		HouseDataPoint dp10 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 57.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(1.5);
	}

	@Test
	public void calculatesTimeBetweenTemps_DoesNotCountCyclesWhereTheTempChanges() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 5.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 5.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp7 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp8 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp9 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp10 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(1.5);
	}

	@Test
	public void calculatesTimeBetweenTemps_TempSettingChangesAtTheEndOfACycleInvalideTheCycle() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp7 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp8 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp9 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp10 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 55.0);
		HouseDataPoint dp11 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10, dp11));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(1);
	}

	@Test
	public void calculatesTimeBetweenTemps_TempSettingChangesAtTheBeginningOfACycleInvalideTheCycle() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp7 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp8 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp9 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp10 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp11 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10, dp11));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(1);
	}

	@Test
	public void calculatesTimeBetweenTemps_FullCyclesAtDifferentTempsAreAllCounted() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 55.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 55.0);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 55.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp7 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp8 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp9 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp10 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp11 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10, dp11));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(2);
	}

	@Test
	public void calculatesTimeBetweenTemps_SkipsCyclesWithMissingMinutesInOffCycle() {
		HouseDataPoint dp1 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp2 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp3 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 55.0);
		HouseDataPoint dp4 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 55.0, true);
		HouseDataPoint dp5 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 55.0);
		HouseDataPoint dp6 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp7 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 55.0);
		HouseDataPoint dp8 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp9 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		HouseDataPoint dp10 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.OFF, 158.0);
		HouseDataPoint dp11 = buildDataPointWithFurnaceStateAndTempSetting(FurnaceState.HEAT_ON, 158.0);
		when(houseStatusRepository.retrieveHouseStatusFrom(any(DateTime.class), any(DateTime.class))).thenReturn(Arrays.asList(dp1, dp2, dp3, dp4, dp5, dp6, dp7, dp8, dp9, dp10, dp11));

		HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(LocalDate.now());

		assertThat(houseDailySummary.getAverageTimeBetweenHeaterCyclesAtOneTemp()).isEqualTo(1);
	}

	private HouseDataPoint buildEmptyDataSet() {
		return new HouseDataPoint(getDateTime(), getDateTime(), nullThermostatStatus(), nullWeatherStatus());
	}

	@NotNull
	private DateTime getDateTime() {
		DateTime newTime = startingTime.plusMinutes(minutesCounter);
		minutesCounter++;
		return newTime;
	}

	private HouseDataPoint buildDataPointWithHouseTemp(Double temp) {
		ThermostatStatus thermostatStatus = new ThermostatStatus(temp, 0.0, null, null);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, thermostatStatus, nullWeatherStatus());
	}

	private HouseDataPoint buildDataPointWitTempSetting(Double tempSetting) {
		ThermostatStatus thermostatStatus = new ThermostatStatus(0.0, tempSetting, null, null);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, thermostatStatus, nullWeatherStatus());
	}

	private HouseDataPoint buildDataPointWithFurnaceState(FurnaceState furnaceState) {
		ThermostatStatus thermostatStatus = new ThermostatStatus(0.0, 0.0, furnaceState, null);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, thermostatStatus, nullWeatherStatus());
	}

	private HouseDataPoint buildDataPointWithThermostatMode(ThermostatMode thermostatMode) {
		ThermostatStatus thermostatStatus = new ThermostatStatus(0.0, 0.0, null, thermostatMode);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, thermostatStatus, nullWeatherStatus());
	}

	private HouseDataPoint buildDataPointWitExternalTemp(Double externalTemp) {
		WeatherStatus weatherStatus = new WeatherStatus(externalTemp, 0.0, 0.0, 0.0);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, nullThermostatStatus(), weatherStatus);
	}

	private HouseDataPoint buildDataPointWithInternalAndExternalTemp(double internalTemp, double externalTemp) {
		ThermostatStatus thermostatStatus = new ThermostatStatus(internalTemp, 0.0, null, null);
		WeatherStatus weatherStatus = new WeatherStatus(externalTemp, 0.0, 0.0, 0.0);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, thermostatStatus, weatherStatus);
	}

	private HouseDataPoint buildDataPointWithWindSpeed(double windSpeed) {
		WeatherStatus weatherStatus = new WeatherStatus(0.0, windSpeed, 0.0, 0.0);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, nullThermostatStatus(), weatherStatus);
	}

	private ThermostatStatus nullThermostatStatus() {
		return new ThermostatStatus(0.0, 0.0, null, null);
	}

	private WeatherStatus nullWeatherStatus() {
		return new WeatherStatus(0.0, 0.0, 0.0, 0.0);
	}

	private HouseDataPoint buildDataPointWithFurnaceStateAndTempSetting(FurnaceState furnaceState, double tempSetting) {
		ThermostatStatus thermostatStatus = new ThermostatStatus(0.0, tempSetting, furnaceState, null);
		DateTime dateTime = getDateTime();
		return new HouseDataPoint(dateTime, dateTime, thermostatStatus, nullWeatherStatus());
	}

	private HouseDataPoint buildDataPointWithFurnaceStateAndTempSetting(FurnaceState furnaceState, double tempSetting, Boolean skipMinute) {
		ThermostatStatus thermostatStatus = new ThermostatStatus(0.0, tempSetting, furnaceState, null);
		DateTime time = getDateTime();
		if (skipMinute) {
			time = getDateTime();
		}

		return new HouseDataPoint(time, time, thermostatStatus, nullWeatherStatus());
	}
}