package se.tornroth.kodi.util;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.THURSDAY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import javax.validation.constraints.AssertFalse;
import java.time.LocalDate;
import java.time.LocalTime;

public class KodiUtilsTest {

	@Test
	public void simularEnough_series() {
		assertTrue(KodiUtils.simularEnough("grizzly and the Lemmings", "Grizzy & the lemmings"));
	}

	@Test
	public void simularEnough_movies() {
		assertTrue(KodiUtils.simularEnough("Sixth sense", "The Sixth Sense"));
        assertFalse(KodiUtils.simularEnough("Die Hard 2", "Die hard"));
	}

	@Test
	public void calculateSimularity_series() {
		assertEquals(KodiUtils.calculateSimularity("grizzly and the Lemmings", "Grizzy & the lemmings"),
				0.8333333333333334d);
	}

	@Test
	public void calculateSimularity_movies() {
		assertEquals(KodiUtils.calculateSimularity("Sixth sense", "The Sixth Sense"),
				1.0d);
		assertEquals(KodiUtils.calculateSimularity("Night to remember", "A night to remember"),
				1.0d);
		assertEquals(KodiUtils.calculateSimularity("Team", "The A Team"),
				1.0d);
        assertEquals(KodiUtils.calculateSimularity("Die hard 2", "Die hard"),
                0.8d);
        assertEquals(KodiUtils.calculateSimularity("Die hard", "Die hard 2"),
                0.8d);
	}

	@Test
	public void getNoOfEpisodesBasedOnWeekday() {
		assertEquals(KodiUtils.getNoOfEpisodesBasedOnWeekday(MONDAY).intValue(), 3);
		assertEquals(KodiUtils.getNoOfEpisodesBasedOnWeekday(THURSDAY).intValue(), 3);

		assertEquals(KodiUtils.getNoOfEpisodesBasedOnWeekday(FRIDAY).intValue(), 2);

		assertEquals(KodiUtils.getNoOfEpisodesBasedOnWeekday(SATURDAY).intValue(), 6);
	}

	@Test
	public void getNoOfEpisodes() {
		assertTrue(KodiUtils.getNoOfEpisodes(6d) > 0);
	}

	@Test
	public void getNoOfEpisodes_weekdays_6_minute_shows() {
		assertEquals(KodiUtils.getNoOfEpisodes(THURSDAY,18,6d).intValue(), 5);
	}

	@Test
	public void getNoOfEpisodes_weekdays_12_minute_shows() {
		assertEquals(KodiUtils.getNoOfEpisodes(MONDAY,18,12d).intValue(), 3);
	}

	@Test
	void getNoOfEpisodes_friday_12_minute_shows() {
		assertEquals(KodiUtils.getNoOfEpisodes(FRIDAY,18,12d).intValue(), 2);
	}

	@Test
	void getNoOfEpisodes_mornings() {
		assertEquals(KodiUtils.getNoOfEpisodes(SATURDAY,7,12d).intValue(), 5);
		assertEquals(KodiUtils.getNoOfEpisodes(FRIDAY,7,12d).intValue(), 5);
		assertEquals(KodiUtils.getNoOfEpisodes(MONDAY,7,6d).intValue(), 10);
		assertEquals(KodiUtils.getNoOfEpisodes(MONDAY,7,12d).intValue(), 5);
	}
}
