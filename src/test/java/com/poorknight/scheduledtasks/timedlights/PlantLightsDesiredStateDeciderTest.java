package com.poorknight.scheduledtasks.timedlights;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.MediaType;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DateTime.class})
public class PlantLightsDesiredStateDeciderTest {

	private PlantLightsDesiredStateDecider decider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(DateTime.class);
		decider = new PlantLightsDesiredStateDecider();
	}

	@Test
	public void returnsStateON_IfBetween7amInclusiveAnd10pmExclusiveDetroitTime() throws Exception {

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T06:59:59-05:00", DateTimeZone.forID("America/Detroit")));
		DesiredState desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T07:00:00-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T07:00:01-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T15:03:04-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T21:59:59-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T22:00:00-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T03:30:30-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);
	}

	@Test
	public void movesAnHourWithDaylightSavings() throws Exception {

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T06:59:59-04:00", DateTimeZone.forID("America/Detroit")));
		DesiredState desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T07:00:00-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T07:00:01-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T15:03:04-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T21:59:59-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T22:00:00-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T03:30:30-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);
	}

}