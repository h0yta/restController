package se.tornroth.kodi;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import se.tornroth.http.HttpService;
import se.tornroth.kodi.entity.Episode;
import se.tornroth.kodi.entity.Mediaplayer;
import se.tornroth.kodi.entity.Movie;
import se.tornroth.kodi.entity.Playlist;

public class KodiPlaylistService extends AbstractKodiService {

	@Inject
	private HttpService httpService;

	public Optional<String> clearPlaylist(Mediaplayer mediaplayer) {
		Playlist playlist = findPlaylists(mediaplayer);

		return Optional.ofNullable(sendClearPlaylist(playlist));
	}

	public Playlist insertEpisodesToPlaylist(List<Episode> episodes) {
		Playlist playlist = findPlaylists(episodes.stream().map(Episode::getMediaplayer).findAny().get());

		int position = 0;
		for (Episode episode : episodes) {
			sendInsertIntoPlaylist(playlist, position++, "episodeid", episode.getEpisodeId());
		}

		return playlist;
	}

	public Playlist addEpisodesToPlaylist(List<Episode> episodes) {
		Playlist playlist = findPlaylists(episodes.stream().map(Episode::getMediaplayer).findAny().get());

		for (Episode episode : episodes) {
			sendAddIntoPlaylist(playlist, "episodeid", episode.getEpisodeId());
		}

		return playlist;
	}

	public void addMoviesToPlaylist(List<Movie> movies) {
		Playlist playlist = findPlaylists(movies.stream().map(Movie::getMediaplayer).findAny().get());
		int position = 0;
		for (Movie movie : movies) {
			sendInsertIntoPlaylist(playlist, position++, "movieid", movie.getMovieId());
		}
	}

	private Playlist findPlaylists(Mediaplayer mediaplayer) {
		String playlistsPaylod = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.GetPlaylists\", \"id\": \"kodiService\"}";
		String playlists = httpService.sendPost(mediaplayer.getUrl(), playlistsPaylod);

		Optional<String> playlistid = findValueFromArray(playlists, "playlistid");
		return new Playlist(mediaplayer, playlistid.orElse(null));
	}

	private String sendClearPlaylist(Playlist playlist) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.Clear\", " + "\"params\": { \"playlistid\": "
				+ playlist.getPlaylistid() + "}, \"id\": \"kodiService\"}";
		return httpService.sendPost(playlist.getMediaplayer().getUrl(), payload);
	}

	private String sendInsertIntoPlaylist(Playlist playlist, Integer position, String itemType, Integer itemId) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.Insert\", "//
				+ "\"params\": { "//
				+ "\"playlistid\": " + playlist.getPlaylistid() + ", "//
				+ "\"position\": " + position + ", "//
				+ "\"item\": {\"" + itemType + "\": " + itemId + "}},"//
				+ "\"id\": \"kodiService\"}";
		return httpService.sendPost(playlist.getMediaplayer().getUrl(), payload);
	}

	private String sendAddIntoPlaylist(Playlist playlist, String itemType, Integer itemId) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.Add\", "//
				+ "\"params\": { "//
				+ "\"playlistid\": " + playlist.getPlaylistid() + ", "//
				+ "\"item\": {\"" + itemType + "\": " + itemId + "}},"//
				+ "\"id\": \"kodiService\"}";
		return httpService.sendPost(playlist.getMediaplayer().getUrl(), payload);
	}
}
