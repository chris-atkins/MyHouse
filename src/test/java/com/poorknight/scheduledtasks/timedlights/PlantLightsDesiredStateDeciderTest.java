package com.poorknight.scheduledtasks.timedlights;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PlantLightsDesiredStateDeciderTest {

	private PlantLightsDesiredStateDecider decider = new PlantLightsDesiredStateDecider();

	@Test
	public void returnsStateON_IfBetween7amInclusiveAnd10pmExclusiveDetroitTime() throws Exception {
		try(MockedStatic<DateTime> mockedDateTime = Mockito.mockStatic(DateTime.class)) {

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T06:59:59-05:00", DateTimeZone.forID("America/Detroit")));
			DesiredState desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.OFF);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T07:00:00-05:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T07:00:01-05:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T15:03:04-05:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T21:59:59-05:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T22:00:00-05:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.OFF);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T03:30:30-05:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.OFF);
		}
	}

	@Test
	public void movesAnHourWithDaylightSavings() {
		try(MockedStatic<DateTime> mockedDateTime = Mockito.mockStatic(DateTime.class)) {

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T06:59:59-04:00", DateTimeZone.forID("America/Detroit")));
			DesiredState desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.OFF);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T07:00:00-04:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T07:00:01-04:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T15:03:04-04:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T21:59:59-04:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.ON);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T22:00:00-04:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.OFF);

			mockedDateTime.when(() -> DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T03:30:30-04:00", DateTimeZone.forID("America/Detroit")));
			desiredState = decider.findDesiredState();
			assertThat(desiredState).isEqualTo(DesiredState.OFF);
		}
	}

}