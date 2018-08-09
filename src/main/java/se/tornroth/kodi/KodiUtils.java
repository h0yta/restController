package se.tornroth.kodi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class KodiUtils {
	private static Map<String, String> translatedTitles;

	static {
		translatedTitles = new HashMap<>();
		translatedTitles.put("curious george", "nicke nyfiken");
		translatedTitles.put("friends", "vänner");
		translatedTitles.put("alfie atkins", "alfons åberg");

	}

	public static Optional<String> translateTitle(String title) {
		if (translatedTitles.containsKey(title.toLowerCase())) {
			return Optional.of(translatedTitles.get(title.toLowerCase()));
		}

		return translatedTitles.keySet().stream().filter(translatedTitle -> {
			return simularEnough(translatedTitle, title);
		}).findFirst();
	}

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
