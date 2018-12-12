package com.poorknight.endpoints;

import com.poorknight.housestatus.reports.HouseStatusReport;
import com.poorknight.housestatus.reports.HouseStatusReporter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/reports")
public class ReportsEndpoint {

	private HouseStatusReporter reporter;

	public ReportsEndpoint(HouseStatusReporter reporter) {
		this.reporter = reporter;
	}

	@GET
	@Path("/last24Hours")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HouseStatusReport reportLast24Hours() {
		return reporter.reportForLast24Hours();
	}
}
