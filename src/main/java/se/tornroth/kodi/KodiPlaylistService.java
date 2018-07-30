package se.tornroth.kodi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import se.tornroth.kodi.entity.Playlist;

public class KodiPlaylistService extends AbstractKodiService {

	private final static List<String> SERVERS = Arrays.asList("http://192.168.1.204:80/jsonrpc",
			"http://192.168.1.205:80/jsonrpc");

	public List<String> clearPlaylist() {
		List<Playlist> playlists = findPlaylists(SERVERS);
		return playlists.stream().map(player -> {
			return sendClearPlaylist(player.getUrl(), player.getPlaylistid());
		}).collect(Collectors.toList());
	}

	private List<Playlist> findPlaylists(List<String> servers) {
		String playlistsPaylod = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.GetPlaylists\", \"id\": \"kodiService\"}";
		return servers.stream().map(server -> {
			String playlists = sendPost(server, playlistsPaylod);

			Optional<String> playlistid = findValueFromArray(playlists, "playlistid");
			return new Playlist(server, playlistid.orElse(null));
		}).filter(playlist -> playlist.hasPlaylistId()).collect(Collectors.toList());
	}

	private String sendClearPlaylist(String url, String playlistid) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.Clear\", " + "\"params\": { \"playlistid\": "
				+ playlistid + "}, \"id\": \"kodiService\"}";
		String result = sendPost(url, payload);
		return result;
	}
}
