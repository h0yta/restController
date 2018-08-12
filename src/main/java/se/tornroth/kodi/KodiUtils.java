package se.tornroth.kodi;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class KodiUtils {

	public static boolean simularEnough(String requested, String tvshow) {
		return calculateSimularity(requested, tvshow) >= 0.83d;
	}

	public static double calculateSimularity(String requested, String tvshow) {
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		double sim = l.distance(requested.toLowerCase(), tvshow.toLowerCase());
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
}
