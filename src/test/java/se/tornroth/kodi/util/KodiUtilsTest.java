package se.tornroth.kodi.util;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.THURSDAY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class KodiUtilsTest {

	@Test
	public void simularEnough() {
		assertTrue(KodiUtils.simularEnough("grizzly and the Lemmings", "Grizzy & the lemmings"));
	}

	@Test
	public void calculateSimularity() {
		assertEquals(KodiUtils.calculateSimularity("grizzly and the Lemmings", "Grizzy & the lemmings"),
				0.8333333333333334d);
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
