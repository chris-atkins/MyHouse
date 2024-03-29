package com.poorknight.echo.housecommand.combination;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Random.class, GoingToWorkResponseBuilder.class})
public class GoingToWorkResponseBuilderTest {

	private GoingToWorkResponseBuilder goingToWorkResponseBuilder;

	@Mock
	private Random random;


	@Before
	public void setUp() throws Exception {
		PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(random);
		goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
	}

	@Test
	public void threeTenthsOfTheTime_RespondsWith_HaveAGoodDay() throws Exception {
		when(random.nextInt(10)).thenReturn(0);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a good day.");

		when(random.nextInt(10)).thenReturn(1);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a good day.");

		when(random.nextInt(10)).thenReturn(2);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a good day.");
	}

	@Test
	public void threeTenthsOfTheTime_RespondsWith_HaveANiceDay() throws Exception {
		when(random.nextInt(10)).thenReturn(3);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a nice day.");

		when(random.nextInt(10)).thenReturn(4);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a nice day.");

		when(random.nextInt(10)).thenReturn(5);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a nice day.");
	}

	@Test
	public void threeTenthsOfTheTime_RespondsWith_SeeYa() throws Exception {
		when(random.nextInt(10)).thenReturn(6);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("See ya.");

		when(random.nextInt(10)).thenReturn(7);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("See ya.");

		when(random.nextInt(10)).thenReturn(8);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("See ya.");
	}

	@Test
	public void oneTenthsOfTheTime_RespondsWith_FuckYou() throws Exception {
		when(random.nextInt(10)).thenReturn(9);
		assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Fuck you.");

	}
}