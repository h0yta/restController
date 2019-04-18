package se.tornroth.kodi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import se.tornroth.http.service.HttpService;
import se.tornroth.kodi.entity.Episode;
import se.tornroth.kodi.entity.Mediaplayer;
import se.tornroth.kodi.entity.Movie;
import se.tornroth.kodi.entity.Player;
import se.tornroth.kodi.entity.Playlist;
import se.tornroth.kodi.entity.Request;
import se.tornroth.kodi.entity.RequestType;

public class KodiPlayerService extends AbstractKodiService {

	@Inject
	private HttpService httpService;

	@Inject
	private KodiPlaylistService kodiPlaylistService;

	@Inject
	private KodiLibaryService kodiLibaryService;

	public Optional<String> pausePlayer(Mediaplayer mediaplayer) {
		List<Player> activePlayers = findActivePlayers(Arrays.asList(mediaplayer));
		return activePlayers.stream().filter(player -> {
			return player.isPlaying();
		}).map(player -> {
			return sendPlayPause(player.getMediaplayer(), player.getPlayerid());
		}).findFirst();
	}

	public Optional<String> resumePlayer(Mediaplayer mediaplayer) {
		List<Player> activePlayers = findActivePlayers(Arrays.asList(mediaplayer));
		return activePlayers.stream().filter(player -> {
			return player.isPaused();
		}).map(player -> {
			return sendPlayPause(player.getMediaplayer(), player.getPlayerid());
		}).findFirst();
	}

	public Optional<String> stopPlayer(Mediaplayer mediaplayer) {
		List<Player> activePlayers = findActivePlayers(Arrays.asList(mediaplayer));
		return activePlayers.stream().map(player -> {
			return sendStop(player.getMediaplayer(), player.getPlayerid());
		}).findFirst();
	}

	public Optional<String> play(Request request) {
		if (request.getType() == RequestType.MOVIE) {
			Optional<Movie> movie = kodiLibaryService.findMovie(request);
			if (movie.isPresent()) {
				return Optional.ofNullable(sendPlayMovie(movie.get()));
			}

			return Optional.empty();
		} else {
			List<Episode> episodes = kodiLibaryService.findEpisodes(request);
			return Optional.ofNullable(sendPlayEpisodes(episodes));
		}
	}

	public Optional<String> queue(Request request) {
		if (request.getType() == RequestType.MOVIE) {
			return Optional.empty();
		} else {
			List<Episode> episodes = kodiLibaryService.findEpisodes(request);
			return Optional.ofNullable(sendQueueEpisodes(episodes));
		}
	}

	private List<Player> findActivePlayers(List<Mediaplayer> mediaplayers) {
		String activePlayersPaylod = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.GetActivePlayers\", \"id\": \"kodiService\"}";
		return mediaplayers.stream().map(mediaplayer -> {
			String activePlayers = httpService.sendPost(mediaplayer.getUrl(), activePlayersPaylod);

			Optional<String> playerid = findValueFromArray(activePlayers, "playerid");
			return new Player(mediaplayer, playerid.orElse(null));
		}).filter(player -> player.hasPlayerId()).map(player -> {
			String playerStatusPayload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.GetProperties\","
					+ "\"params\": {\"playerid\": " + player.getPlayerid()
					+ "	, \"properties\": [\"speed\"]}, \"id\": \"kodiService\"}";
			String playerStatus = httpService.sendPost(player.getMediaplayer().getUrl(), playerStatusPayload);

			Optional<String> speed = findValue(playerStatus, "speed");
			return new Player(player.getMediaplayer(), player.getPlayerid(), speed.orElse(null));
		}).collect(Collectors.toList());
	}

	private String sendPlayPause(Mediaplayer mediaplayer, String playerid) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.PlayPause\", " + "\"params\": { \"playerid\": "
				+ playerid + "}, \"id\": \"kodiService\"}";
		String result = httpService.sendPost(mediaplayer.getUrl(), payload);
		return result;
	}

	private String sendStop(Mediaplayer mediaplayer, String playerid) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Stop\", " + "\"params\": { \"playerid\": "
				+ playerid + "}, \"id\": \"kodiService\"}";
		String result = httpService.sendPost(mediaplayer.getUrl(), payload);
		return result;
	}

	private String sendPlayEpisodes(List<Episode> episodes) {
		if (episodes.isEmpty()) {
			return "Empty";
		}

		// This looks strange, but there is a bug in kodi.
		// So if the playlist is cleared and all episodes are added to playlist
		// the menu will be shown while playing the first episode.
		// this doesn't happen if the playlist is cleared and one episode is
		// added, the player is opened with that playlist and the rest of the
		// episodes is then added to the playlist.
		kodiPlaylistService.clearPlaylist(episodes.stream().map(Episode::getMediaplayer).findFirst().get());
		Playlist playlist = kodiPlaylistService.addEpisodesToPlaylist(episodes.subList(0, 1));
		String playresult = sendPlayPlaylist(playlist);

		if (episodes.size() > 1) {
			kodiPlaylistService.addEpisodesToPlaylist(episodes.subList(1, episodes.size()));
		}

		return playresult;
	}

	private String sendQueueEpisodes(List<Episode> episodes) {
		if (episodes.isEmpty()) {
			return "Empty";
		}

		kodiPlaylistService.addEpisodesToPlaylist(episodes);

		return "Queued";
	}

	@SuppressWarnings("unused")
	private String sendPlayEpisode(Episode episode) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", "
				+ "\"params\": {\"item\": {\"episodeid\": " + episode.getEpisodeId() + "}},"
				+ "\"id\": \"kodiService\"}";

		return httpService.sendPost(episode.getMediaplayer().getUrl(), payload);
	}

	private String sendPlayPlaylist(Playlist playlist) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", "
				+ "\"params\": {\"item\": {\"playlistid\": " + playlist.getPlaylistid() + "}},"
				+ "\"id\": \"kodiService\"}";

		return httpService.sendPost(playlist.getMediaplayer().getUrl(), payload);
	}

	private String sendPlayMovie(Movie movie) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", "
				+ "\"params\": {\"item\": {\"movieid\": " + movie.getMovieId() + "}}," + "\"id\": \"kodiService\"}";

		return httpService.sendPost(movie.getMediaplayer().getUrl(), payload);
	}
}
