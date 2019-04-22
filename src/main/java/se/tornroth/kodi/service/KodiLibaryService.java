package se.tornroth.kodi.service;

import static se.tornroth.kodi.util.KodiUtils.getNoOfEpisodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import se.tornroth.http.service.HttpService;
import se.tornroth.kodi.entity.Episode;
import se.tornroth.kodi.entity.Mediaplayer;
import se.tornroth.kodi.entity.Movie;
import se.tornroth.kodi.entity.Request;
import se.tornroth.kodi.entity.Tvshow;
import se.tornroth.kodi.util.EpisodeLengthHelper;
import se.tornroth.kodi.util.KodiUtils;
import se.tornroth.kodi.util.TranslationHelper;

public class KodiLibaryService extends AbstractKodiService {

	@Inject
	private HttpService httpService;

	@Inject
	private TranslationHelper translationHelper;

	@Inject
	private EpisodeLengthHelper episodeLengthHelper;

	public Optional<String> scanLibrary(Mediaplayer mediaplayer) {
		return Optional.ofNullable(sendScan(mediaplayer.getUrl()));
	}

	public Optional<String> cleanLibrary(Mediaplayer mediaplayer) {
		return Optional.ofNullable(sendClean(mediaplayer.getUrl()));
	}

	public List<Episode> findEpisodes(Request request) {
		Optional<Tvshow> matchedTvshow = findTvShow(request);

		if (matchedTvshow.isPresent()) {
			return findEpisodes(request, matchedTvshow.get());
		}

		System.out.println("Nothing to be played found for episode request: " + request);
		return Collections.emptyList();
	}

	public Optional<Movie> findMovie(Request request) {
		List<Movie> movies = fetchMovies(request.getMediaplayer());
		switch (request.getType()) {
		case MOVIE:
			return movies.stream().filter(movie -> {
				return KodiUtils.simularEnough(request.getTitle(), movie.getTitle());
			}).findFirst();
		default:
			throw new IllegalArgumentException("Unknown type");
		}
	}

	private Optional<Tvshow> findTvShow(Request request) {
		return fetchTvShows(request.getMediaplayer()).stream().filter(tvshow -> {
			if (KodiUtils.simularEnough(request.getTitle(), tvshow.getTitle())) {
				return true;
			}

			Optional<String> optTitle = translationHelper.translateTitle(request.getTitle());

			return optTitle.filter(s -> KodiUtils.simularEnough(s, tvshow.getTitle())).isPresent();
		}).findFirst();
	}

	private List<Episode> findEpisodes(Request requestedTvshow, Tvshow matchedTvshow) {
		List<Episode> episodes = fetchEpisodes(matchedTvshow);

		switch (requestedTvshow.getType()) {
		case TITLE:
			Optional<Episode> titleMatch = episodes.stream().filter(episode -> {
				return KodiUtils.simularEnough(requestedTvshow.getEpisodeTitle(), episode.findEpisodeTitle());
			}).findFirst();

			if (titleMatch.isPresent()) {
				int index = episodes.indexOf(titleMatch.get());
				return episodes.stream().skip(index).limit(getNoOfEpisodes()).collect(Collectors.toList());
			}

			return Collections.emptyList();
		case SPECIFIC:
			Optional<Episode> specificMatch = episodes.stream().filter(episode -> {
				return episode.findSeasonAndEpisode().equals(requestedTvshow.getEpisodeDescShort());
			}).findFirst();

			if (specificMatch.isPresent()) {
				int index = episodes.indexOf(specificMatch.get());
				return episodes.stream().skip(index).limit(getNoOfEpisodes()).collect(Collectors.toList());
			}

			return Collections.emptyList();
		case RANDOM:
			int rand = new Random().nextInt(episodes.size());
			return episodes.stream().skip(rand)
					.limit(getNoOfEpisodes(episodeLengthHelper.findEpisodeLength(requestedTvshow.getTitle())))
					.collect(Collectors.toList());
		case LATEST:
			return episodes.stream().filter(episode -> !episode.isWatched())
					.sorted(Comparator.comparing(Episode::getSeason).thenComparing(Episode::getEpisode).reversed())
					.limit(1).collect(Collectors.toList());
		case NEXT:
			return episodes.stream().filter(episode -> !episode.isWatched())
					.sorted(Comparator.comparing(Episode::getSeason).thenComparing(Episode::getEpisode)).limit(1)
					.collect(Collectors.toList());
		default:
			return Collections.emptyList();
		}
	}

	private String sendScan(String url) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.Scan\", " + "\"id\": \"kodiService\"}";
		return httpService.sendPost(url, payload);
	}

	private String sendClean(String url) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.Clean\", " + "\"id\": \"kodiService\"}";
		return httpService.sendPost(url, payload);
	}

	private List<Movie> fetchMovies(Mediaplayer mediaplayer) {
		List<Movie> movies = new ArrayList<>();
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetMovies\", "
				+ "\"params\": { \"properties\": [\"playcount\"]}, " + "\"id\": \"kodiService\"}";
		String result = httpService.sendPost(mediaplayer.getUrl(), payload);

		JsonElement root = new JsonParser().parse(result);
		JsonObject resultObject = root.getAsJsonObject().get("result").getAsJsonObject();
		JsonArray moviesArray = resultObject.get("movies").getAsJsonArray();
		if (moviesArray.size() > 0) {
			for (JsonElement elem : moviesArray) {
				JsonElement label = elem.getAsJsonObject().get("label");
				JsonElement movieid = elem.getAsJsonObject().get("movieid");
				JsonElement playcount = elem.getAsJsonObject().get("playcount");

				movies.add(new Movie(mediaplayer, movieid.getAsInt(), label.getAsString(), playcount.getAsInt() > 0));
			}
		}

		return movies;
	}

	private List<Tvshow> fetchTvShows(Mediaplayer mediaplayer) {
		List<Tvshow> tvshows = new ArrayList<>();
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetTVShows\", "
				+ "\"id\": \"kodiService\"}";
		String result = httpService.sendPost(mediaplayer.getUrl(), payload);

		JsonElement root = new JsonParser().parse(result);
		JsonObject resultObject = root.getAsJsonObject().get("result").getAsJsonObject();
		JsonArray tvshowArray = resultObject.get("tvshows").getAsJsonArray();
		if (tvshowArray.size() > 0) {
			for (JsonElement elem : tvshowArray) {
				JsonElement label = elem.getAsJsonObject().get("label");
				JsonElement tvshowid = elem.getAsJsonObject().get("tvshowid");
				tvshows.add(new Tvshow(mediaplayer, label.getAsString(), tvshowid.getAsInt()));
			}
		}

		return tvshows;
	}

	private List<Episode> fetchEpisodes(Tvshow tvshow) {
		List<Episode> seasons = new ArrayList<>();
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetEpisodes\", "
				+ "\"params\": {\"tvshowid\": " + tvshow.getId() + ", "
				+ "\"properties\": [\"season\", \"episode\", \"playcount\"]}, \"id\": \"kodiService\"}";
		String result = httpService.sendPost(tvshow.getMediaplayer().getUrl(), payload);

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

				seasons.add(new Episode(tvshow.getMediaplayer(), tvshow.getId(), episodeid.getAsInt(),
						season.getAsInt(), episode.getAsInt(), label.getAsString(), playcount.getAsInt() > 0));
			}
		}

		return seasons;
	}

}
