package se.tornroth.kodi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.tornroth.kodi.entity.Mediaplayer;
import se.tornroth.kodi.entity.Request;
import se.tornroth.kodi.entity.RequestType;

public class RequestService {
	public Request createRequest(Mediaplayer mediaplayer, String request) {
		if (requestIsSpecifiedEpisode(request)) {
			return getShowFromSpecifiedEpisode(mediaplayer, request);
		} else if (requestIsMovie(request)) {
			return new Request(RequestType.MOVIE, mediaplayer, KodiUtils.stripRequest(request));
		} else if (requestIsLatest(request)) {
			return new Request(RequestType.LATEST, mediaplayer, KodiUtils.stripRequest(request));
		} else if (requestIsNext(request)) {
			return new Request(RequestType.NEXT, mediaplayer, KodiUtils.stripRequest(request));
		} else if (requestIsRandom(request)) {
			return new Request(RequestType.RANDOM, mediaplayer, KodiUtils.stripRequest(request));
		} else if (requestIsEpisodeTitle(request)) {
			return getShowFromEpisodeTitle(mediaplayer, request);
		}

		throw new IllegalArgumentException("Not implemented yet");
	}

	protected static boolean requestIsSpecifiedEpisode(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request.toLowerCase().trim());
		return matcher.matches();
	}

	protected static boolean requestIsEpisodeTitle(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sepisode\\s(.*)");
		Matcher matcher = pattern.matcher(request.toLowerCase().trim());
		return matcher.matches();
	}

	protected static boolean requestIsLatest(String request) {
		Pattern pattern = Pattern.compile("^latest\\s(.*)");
		Matcher matcher = pattern.matcher(request.toLowerCase().trim());
		return matcher.matches();
	}

	protected static boolean requestIsNext(String request) {
		Pattern pattern = Pattern.compile("^next\\s(.*)");
		Matcher matcher = pattern.matcher(request.toLowerCase().trim());
		return matcher.matches();
	}

	protected static boolean requestIsRandom(String request) {
		Pattern pattern = Pattern.compile("^random\\s(.*)");
		Matcher matcher = pattern.matcher(request.toLowerCase().trim());
		return matcher.matches();
	}

	protected static boolean requestIsMovie(String request) {
		Pattern pattern = Pattern.compile("^movie\\s(.*)");
		Matcher matcher = pattern.matcher(request.toLowerCase().trim());
		return matcher.matches();
	}

	protected static Request getShowFromSpecifiedEpisode(Mediaplayer mediaplayer, String request) {
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request);
		while (matcher.find()) {
			return new Request(RequestType.SPECIFIC, mediaplayer, matcher.group(1), matcher.group(2), matcher.group(3));
		}

		throw new IllegalArgumentException("Invalid request string");
	}

	protected static Request getShowFromEpisodeTitle(Mediaplayer mediaplayer, String request) {
		Pattern pattern = Pattern.compile("(.*)\\sepisode\\s(.*)");
		Matcher matcher = pattern.matcher(request);
		while (matcher.find()) {
			return new Request(RequestType.TITLE, mediaplayer, matcher.group(1), matcher.group(2));
		}

		throw new IllegalArgumentException("Invalid request string");
	}

}
