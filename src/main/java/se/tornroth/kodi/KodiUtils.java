package se.tornroth.kodi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import se.tornroth.kodi.entity.Mediaplayer;

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
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		double sim = l.distance(requested, tvshow);
		return (1.0d - sim) >= 0.7d;
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
				.replace("episode", "");
	}

	public static se.tornroth.kodi.entity.Mediaplayer findMediaplayer(String location) {
		if (location.toUpperCase().equals("BASEMENT")) {
			return Mediaplayer.BASEMENT;
		} else if (location.toUpperCase().equals("LIVINGROOM")) {
			return Mediaplayer.LIVINGROOM;
		} else {
			return Mediaplayer.BASEMENT;
		}
	}

	public static String getUrl(Mediaplayer mediaPlayer) {
		if (mediaPlayer == Mediaplayer.LIVINGROOM) {
			return "http://192.168.1.205:80/jsonrpc";
		} else if (mediaPlayer == Mediaplayer.BASEMENT) {
			return "http://192.168.1.204:80/jsonrpc";
		}

		throw new IllegalArgumentException("Unknown mediaplayer");
	}
}
