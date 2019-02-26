package com.poorknight.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.housestatus.reports.HouseDailySummary;
import com.poorknight.housestatus.reports.HouseDailySummaryReporter;
import com.poorknight.housestatus.reports.HouseStatusReport;
import com.poorknight.housestatus.reports.HouseStatusReporter;
import com.poorknight.time.TimeFinder;
import org.joda.time.LocalDate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	public String reportLast24Hours() {
		try {
			HouseStatusReport houseStatusReport = houseStatusReporter.reportForLast24Hours();
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
		try {
			LocalDate date = timeFinder.getCurrentLocalTime().minusDays(1).toLocalDate();
			HouseDailySummary houseDailySummary = houseDailySummaryReporter.summaryForDay(date);
			return new ObjectMapper().writeValueAsString(houseDailySummary);

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
