package se.tornroth.kodi.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

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
		return getNoOfEpisodes(LocalDate.now().getDayOfWeek(), episodeLength);
	}

	static Integer getNoOfEpisodes(final DayOfWeek weekDay, final Double episodeLength) {
		final Integer playtime = getPlaytimeBasedOnWeekday(weekDay);
		final Double ceiled = Math.ceil(playtime.doubleValue() / episodeLength.doubleValue());
		return ceiled.intValue();
	}

	static Integer getPlaytimeBasedOnWeekday(final DayOfWeek weekDay) {
		switch (weekDay) {
		case MONDAY:
		case TUESDAY:
		case WEDNESDAY:
		case THURSDAY:
			return 30;
		case FRIDAY:
			return 20;
		case SATURDAY:
		case SUNDAY:
		default:
			return 60;
		}
	}
}
