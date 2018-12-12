package com.poorknight.housestatus.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class HouseStatusReportTest {


	@Test
	public void isJsonable() throws IOException {
		List<String> localTimes = Arrays.asList("hi", "there");
		List<Double> houseTemperatures = Arrays.asList(3.4, 5.6);
		List<Double> thermostatSettings = Arrays.asList(1.2,2.3);
		HouseStatusReport report = new HouseStatusReport(localTimes, houseTemperatures, thermostatSettings);


		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(report);
		HouseStatusReport readReport = objectMapper.readValue(jsonString, HouseStatusReport.class);

		assertThat(report).isEqualTo(readReport);
	}

	@Test
	public void isImmutable() {
		List<String> localTimes = new ArrayList<>();
		List<Double> houseTemperatures = new ArrayList<>();
		List<Double> thermostatSettings = new ArrayList<>();
		HouseStatusReport report = new HouseStatusReport(localTimes, houseTemperatures, thermostatSettings);

		try {
			report.getLocalTimes().add("hi");
			fail("expecting exception");
		} catch(Exception e) {
			assertThat(e).isInstanceOf(UnsupportedOperationException.class);
		}

		try {
			report.getHouseTemperatures().add(1d);
			fail("expecting exception");
		} catch(Exception e) {
			assertThat(e).isInstanceOf(UnsupportedOperationException.class);
		}

		try {
			report.getThermostatSettings().add(1d);
			fail("expecting exception");
		} catch(Exception e) {
			assertThat(e).isInstanceOf(UnsupportedOperationException.class);
		}
	}
}