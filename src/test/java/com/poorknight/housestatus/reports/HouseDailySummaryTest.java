package com.poorknight.housestatus.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

public class HouseDailySummaryTest {

	@Test
	public void canBeSerializedAndDeserializedAndRemainEqual() throws Exception {
		HouseDailySummary houseDailySummary = new HouseDailySummary(RandomUtils.nextInt(0, 100), RandomUtils.nextInt(0, 100), RandomUtils.nextInt(0, 100), RandomUtils.nextDouble(0, 10), RandomUtils.nextDouble(0, 10), RandomUtils.nextDouble(0, 10), RandomUtils.nextDouble(0, 10), RandomUtils.nextDouble(0, 10), RandomUtils.nextDouble(0, 100), RandomUtils.nextDouble(0, 100));

		String serializedSummary = new ObjectMapper().writeValueAsString(houseDailySummary);
		HouseDailySummary deserializedSummary = new ObjectMapper().readValue(serializedSummary, HouseDailySummary.class);

		assertThat(houseDailySummary).isEqualTo(deserializedSummary);
	}
}