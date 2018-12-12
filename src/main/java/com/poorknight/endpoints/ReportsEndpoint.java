package com.poorknight.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poorknight.housestatus.reports.HouseStatusReport;
import com.poorknight.housestatus.reports.HouseStatusReporter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class ReportsEndpoint {

	private HouseStatusReporter reporter;

	public ReportsEndpoint(HouseStatusReporter reporter) {
		this.reporter = reporter;
	}

	@GET
	@Path("/last24Hours")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String reportLast24Hours() {
		HouseStatusReport houseStatusReport = reporter.reportForLast24Hours();

		try {
			return new ObjectMapper().writeValueAsString(houseStatusReport);

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
