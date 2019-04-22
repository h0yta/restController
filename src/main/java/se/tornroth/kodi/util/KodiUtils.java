package se.tornroth.kodi.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class KodiUtils {

	public static boolean simularEnough(String requested, String tvshow) {
		return calculateSimularity(requested, tvshow) >= 0.83d;
	}

	public static double calculateSimularity(String requested, String tvshow) {
		final NormalizedLevenshtein l = new NormalizedLevenshtein();
		final double sim = l.distance(requested.toLowerCase(), tvshow.toLowerCase());
		return (1.0d - sim);
	}

	public static String stripRequest(String request) {
		return request.toLowerCase()//
				.replace("latest episode of", "")//
				.replace("latest episode", "")//
				.replace("latest", "")//
				.replace("next episode of", "")//
				.replace("next episode", "")//
				.replace("next", "").replace("random episode of", "")//
				.replace("random episode", "")//
				.replace("random", "")//
				.replace("episode", "")//
				.replace("movie", "")//
				.trim();
	}

	public static Integer getNoOfEpisodes() {
		return getNoOfEpisodesBasedOnWeekday(LocalDate.now().getDayOfWeek());
	}

	static Integer getNoOfEpisodesBasedOnWeekday(final DayOfWeek weekDay) {
		switch (weekDay) {
		case MONDAY:
		case TUESDAY:
		case WEDNESDAY:
		case THURSDAY:
			return 3;
		case FRIDAY:
			return 2;
		case SATURDAY:
		case SUNDAY:
		default:
			return 6;
		}
	}

	public static Integer getNoOfEpisodes(final Double episodeLength) {
		return getNoOfEpisodes(LocalDate.now().getDayOfWeek(), LocalTime.now().getHour(), episodeLength);
	}

	static Integer getNoOfEpisodes(final DayOfWeek weekDay, final int hour, final Double episodeLength) {
		final Integer playtime = getPlaytimeBasedOnWeekday(weekDay, hour);
		final Double ceil = Math.ceil(playtime.doubleValue() / episodeLength);
		return ceil.intValue();
	}

	private static Integer getPlaytimeBasedOnWeekday(final DayOfWeek weekDay, final int hour) {
		if (hour < 9) {
			return 60;
		}

		switch (weekDay) {
		case MONDAY:
		case TUESDAY:
		case WEDNESDAY:
		case THURSDAY:
			return 30;
		case FRIDAY:
		case SATURDAY:
		case SUNDAY:
		default:
			return 20;
		}
	}
}
