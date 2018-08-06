package se.tornroth.kodi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import se.tornroth.kodi.entity.Episode;
import se.tornroth.kodi.entity.RequestedTvshow;
import se.tornroth.kodi.entity.RequestedTvshow.Mediaplayer;
import se.tornroth.kodi.entity.RequestedTvshow.Type;
import se.tornroth.kodi.entity.Tvshow;

public class KodiLibaryService extends AbstractKodiService {

	private static final List<String> SERVERS = Arrays.asList("http://192.168.1.204:80/jsonrpc");

	public List<String> scan() {
		return SERVERS.stream().map(server -> {
			return sendScan(server);
		}).collect(Collectors.toList());
	}

	public List<String> clean() {
		return SERVERS.stream().map(server -> {
			return sendClean(server);
		}).collect(Collectors.toList());
	}

	public String playEpisode(String location, String request) {
		RequestedTvshow requestedTvshow = getShowFromRequest(location, request);
		Optional<Tvshow> matchedTvshow = findTvShow(requestedTvshow);

		if (matchedTvshow.isPresent()) {
			Optional<Episode> matchedEpisode = findEpisode(requestedTvshow, matchedTvshow);

			if (matchedEpisode.isPresent()) {
				return sendPlayEpisode(matchedEpisode.get());
			}
		}

		System.out.println("Nothing to be played found for request: " + request);
		return "ERROR";
	}

	private Optional<Episode> findEpisode(RequestedTvshow requestedTvshow, Optional<Tvshow> matchedTvshow) {
		List<Episode> episodes = findEpisodes(matchedTvshow.get());

		switch (requestedTvshow.getType()) {
		case EPISODE_TITLE:
			return episodes.stream().filter(episode -> {
				return KodiUtils.simularEnough(requestedTvshow.getEpisodeTitle(), episode.findEpisodeTitle());
			}).findFirst();
		case SPECIFIC_EPISODE:
			return episodes.stream().filter(episode -> {
				return episode.findSeasonAndEpisode().equals(requestedTvshow.getEpisodeDescShort());
			}).findFirst();
		case LATEST_EPISODE:
			return episodes.stream().filter(episode -> !episode.isWatched())
					.sorted(Comparator.comparing(Episode::getSeason).thenComparing(Episode::getEpisode).reversed())
					.findFirst();
		case NEXT_EPISODE:
			return episodes.stream().filter(episode -> !episode.isWatched())
					.sorted(Comparator.comparing(Episode::getSeason).thenComparing(Episode::getEpisode)).findFirst();
		case RANDOM_EPISODE:
			Collections.shuffle(episodes);
			return episodes.stream().findFirst();
		default:
			throw new IllegalArgumentException("Unknown type");
		}
	}

	private Optional<Tvshow> findTvShow(RequestedTvshow requestedTvshow) {
		Optional<Tvshow> matchedTvshow = findTvShows(KodiUtils.getUrl(requestedTvshow.getMediaplayer())).stream()
				.filter(tvshow -> {
					if (KodiUtils.simularEnough(requestedTvshow.getTitle(), tvshow.getTitle())) {
						return true;
					}

					Optional<String> optTitle = KodiUtils.translateTitle(requestedTvshow.getTitle());

					if (optTitle.isPresent()) {
						return KodiUtils.simularEnough(optTitle.get(), tvshow.getTitle());
					}

					return false;
				}).findFirst();

		return matchedTvshow;
	}

	public String playMovie(String request) {

		return "";
	}

	private String sendScan(String url) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.Scan\", " + "\"id\": \"kodiService\"}";
		String result = sendPost(url, payload);
		return result;
	}

	private String sendClean(String url) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.Clean\", " + "\"id\": \"kodiService\"}";
		String result = sendPost(url, payload);
		return result;
	}

	private String sendPlayEpisode(Episode episode) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", "
				+ "\"params\": {\"item\": {\"episodeid\": " + episode.getEpisodeId() + "}},"
				+ "\"id\": \"kodiService\"}";

		return sendPost(episode.getUrl(), payload);
	}

	private List<Tvshow> findTvShows(String url) {
		List<Tvshow> tvshows = new ArrayList<>();
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetTVShows\", "
				+ "\"id\": \"kodiService\"}";
		String result = sendPost(url, payload);

		JsonElement root = new JsonParser().parse(result);
		JsonObject resultObject = root.getAsJsonObject().get("result").getAsJsonObject();
		JsonArray tvshowArray = resultObject.get("tvshows").getAsJsonArray();
		if (tvshowArray.size() > 0) {
			for (JsonElement elem : tvshowArray) {
				JsonElement label = elem.getAsJsonObject().get("label");
				JsonElement tvshowid = elem.getAsJsonObject().get("tvshowid");
				tvshows.add(new Tvshow(url, label.getAsString(), tvshowid.getAsInt()));
			}
		}

		return tvshows;
	}

	private List<Episode> findEpisodes(Tvshow tvshow) {
		List<Episode> seasons = new ArrayList<>();
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetEpisodes\", "
				+ "\"params\": {\"tvshowid\": " + tvshow.getId() + ", "
				+ "\"properties\": [\"season\", \"episode\", \"playcount\"]}, \"id\": \"kodiService\"}";
		String result = sendPost(tvshow.getUrl(), payload);

		JsonElement root = new JsonParser().parse(result);
		JsonObject resultObject = root.getAsJsonObject().get("result").getAsJsonObject();
		JsonArray tvshowArray = resultObject.get("episodes").getAsJsonArray();
		if (tvshowArray.size() > 0) {
			for (JsonElement elem : tvshowArray) {
				JsonElement episodeid = elem.getAsJsonObject().get("episodeid");
				JsonElement season = elem.getAsJsonObject().get("season");
				JsonElement episode = elem.getAsJsonObject().get("episode");
				JsonElement label = elem.getAsJsonObject().get("label");
				JsonElement playcount = elem.getAsJsonObject().get("playcount");

				seasons.add(new Episode(tvshow.getUrl(), tvshow.getId(), episodeid.getAsInt(), season.getAsInt(),
						episode.getAsInt(), label.getAsString(), playcount.getAsInt() > 0));
			}
		}

		return seasons;
	}

	protected static RequestedTvshow getShowFromRequest(String location, String request) {
		if (requestIsSpecifiedEpisode(request)) {
			return getShowFromSpecifiedEpisode(location, request);
		} else if (requestIsLatestEpisode(request)) {
			return new RequestedTvshow(Type.LATEST_EPISODE, KodiUtils.findMediaplayer(location),
					KodiUtils.stripRequest(request));
		} else if (requestIsNextEpisode(request)) {
			return new RequestedTvshow(Type.NEXT_EPISODE, KodiUtils.findMediaplayer(location),
					KodiUtils.stripRequest(request));
		} else if (requestIsRandomEpisode(request)) {
			return new RequestedTvshow(Type.RANDOM_EPISODE, KodiUtils.findMediaplayer(location),
					KodiUtils.stripRequest(request));
		} else if (requestIsEpisodeTitle(request)) {
			return getShowFromEpisodeTitle(location, request);
		}

		throw new IllegalArgumentException("Not implemented yet");
	}

	protected static boolean requestIsSpecifiedEpisode(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request.trim());
		return matcher.matches();
	}

	protected static boolean requestIsEpisodeTitle(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sepisode\\s(.*)");
		Matcher matcher = pattern.matcher(request.trim());
		return matcher.matches();
	}

	protected static boolean requestIsLatestEpisode(String request) {
		Pattern pattern = Pattern.compile("^latest\\s(.*)");
		Matcher matcher = pattern.matcher(request.trim());
		return matcher.matches();
	}

	protected static boolean requestIsNextEpisode(String request) {
		Pattern pattern = Pattern.compile("^next\\s(.*)");
		Matcher matcher = pattern.matcher(request.trim());
		return matcher.matches();
	}

	protected static boolean requestIsRandomEpisode(String request) {
		Pattern pattern = Pattern.compile("^random\\s(.*)");
		Matcher matcher = pattern.matcher(request.trim());
		return matcher.matches();
	}

	protected static RequestedTvshow getShowFromSpecifiedEpisode(String location, String request) {
		Mediaplayer mediaplayer = KodiUtils.findMediaplayer(location);
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request);
		while (matcher.find()) {
			return new RequestedTvshow(Type.SPECIFIC_EPISODE, mediaplayer, matcher.group(1), matcher.group(2),
					matcher.group(3));
		}

		throw new IllegalArgumentException("Invalid request string");
	}

	protected static RequestedTvshow getShowFromEpisodeTitle(String location, String request) {
		Mediaplayer mediaplayer = KodiUtils.findMediaplayer(location);
		Pattern pattern = Pattern.compile("(.*)\\sepisode\\s(.*)");
		Matcher matcher = pattern.matcher(request);
		while (matcher.find()) {
			return new RequestedTvshow(Type.EPISODE_TITLE, mediaplayer, matcher.group(1), matcher.group(2));
		}

		throw new IllegalArgumentException("Invalid request string");
	}

}
