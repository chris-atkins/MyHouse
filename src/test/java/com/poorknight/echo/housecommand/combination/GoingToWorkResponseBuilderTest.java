package com.poorknight.echo.housecommand.combination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoingToWorkResponseBuilderTest {

	private GoingToWorkResponseBuilder goingToWorkResponseBuilder;

	@Test
	public void threeTenthsOfTheTime_RespondsWith_HaveAGoodDay() throws Exception {
		try(MockedConstruction<Random> mockedRandom = Mockito.mockConstruction(Random.class)) {

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(0).nextInt(10)).thenReturn(0);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a good day.");

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(1).nextInt(10)).thenReturn(1);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a good day.");

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(2).nextInt(10)).thenReturn(2);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a good day.");
		}
	}

	@Test
	public void threeTenthsOfTheTime_RespondsWith_HaveANiceDay() throws Exception {
		try(MockedConstruction<Random> mockedRandom = Mockito.mockConstruction(Random.class)) {

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(0).nextInt(10)).thenReturn(3);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a nice day.");

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(1).nextInt(10)).thenReturn(4);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a nice day.");

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(2).nextInt(10)).thenReturn(5);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Have a nice day.");
		}
	}

	@Test
	public void threeTenthsOfTheTime_RespondsWith_SeeYa() throws Exception {
		try(MockedConstruction<Random> mockedRandom = Mockito.mockConstruction(Random.class)) {

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(0).nextInt(10)).thenReturn(6);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("See ya.");

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(1).nextInt(10)).thenReturn(7);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("See ya.");

			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(2).nextInt(10)).thenReturn(8);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("See ya.");
		}
	}

	@Test
	public void oneTenthsOfTheTime_RespondsWith_FuckYou() throws Exception {
		try(MockedConstruction<Random> mockedRandom = Mockito.mockConstruction(Random.class)) {
			goingToWorkResponseBuilder = new GoingToWorkResponseBuilder();
			when(mockedRandom.constructed().get(0).nextInt(10)).thenReturn(9);
			assertThat(goingToWorkResponseBuilder.buildHouseCommandAlexaResponse()).isEqualTo("Fuck you.");
		}

	}
}