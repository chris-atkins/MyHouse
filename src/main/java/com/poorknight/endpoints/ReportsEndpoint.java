package com.poorknight.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.housestatus.reports.HouseDailySummary;
import com.poorknight.housestatus.reports.HouseDailySummaryReporter;
import com.poorknight.housestatus.reports.HouseStatusReport;
import com.poorknight.housestatus.reports.HouseStatusReporter;
import com.poorknight.time.TimeFinder;
import org.joda.time.LocalDate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class ReportsEndpoint {

	private HouseStatusReporter houseStatusReporter;
	private HouseDailySummaryReporter houseDailySummaryReporter;
	private TimeFinder timeFinder;

	public ReportsEndpoint(HouseStatusReporter houseStatusReporter, HouseDailySummaryReporter houseDailySummaryReporter, TimeFinder timeFinder) {
		this.houseStatusReporter = houseStatusReporter;
		this.houseDailySummaryReporter = houseDailySummaryReporter;
		this.timeFinder = timeFinder;
	}

	@GET
	@Path("/last24Hours")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String reportLastDay() {
		try {
			LocalDate yesterday = getYesterdaysDate();
			HouseStatusReport houseStatusReport = houseStatusReporter.reportForDay(yesterday);
			return new ObjectMapper().writeValueAsString(houseStatusReport);

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@GET
	@Path("/daily-summary")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String singleDaySummary() {
		LocalDate yesterday = getYesterdaysDate();
		return retrieveSummaryForDay(yesterday);
	}

	@GET
	@Path("/daily-summary/{dateString}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String singleDaySummaryForDay(@PathParam("dateString") String dateString) {
		LocalDate date = LocalDate.parse(dateString);
		return retrieveSummaryForDay(date);
	}

	private String retrieveSummaryForDay(LocalDate yesterday) {
		try {
			HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(yesterday);
			return new ObjectMapper().writeValueAsString(houseDailySummary);

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private LocalDate getYesterdaysDate() {
		return timeFinder.getCurrentLocalTime().minusDays(1).toLocalDate();
	}
}
