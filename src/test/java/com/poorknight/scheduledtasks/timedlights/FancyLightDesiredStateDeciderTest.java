package com.poorknight.scheduledtasks.timedlights;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DateTime.class})
public class FancyLightDesiredStateDeciderTest {

	private FancyLightDesiredStateDecider decider;

	@Before
	public void setup() {
		PowerMockito.mockStatic(DateTime.class);
		decider = new FancyLightDesiredStateDecider();
	}

	@Test
	public void returnsStateON_IfBetween10pmInclusiveAnd4amExclusiveDetroitTime() throws Exception {
		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T21:59:59-05:00", DateTimeZone.forID("America/Detroit")));
		DesiredState desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T22:00:00-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T22:00:01-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T00:01:02-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T03:59:59-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T04:00:00-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-12-05T12:30:30-05:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);
	}

	@Test
	public void movesAnHourWithDaylightSavings() throws Exception {
		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T21:59:59-04:00", DateTimeZone.forID("America/Detroit")));
		DesiredState desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T22:00:00-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T22:00:01-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T01:03:04-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T03:59:59-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.ON);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T04:00:00-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);

		PowerMockito.when(DateTime.now(DateTimeZone.forID("America/Detroit"))).thenReturn(new DateTime("2021-08-05T12:30:30-04:00", DateTimeZone.forID("America/Detroit")));
		desiredState = decider.findDesiredState();
		assertThat(desiredState).isEqualTo(DesiredState.OFF);
	}

}