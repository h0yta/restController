package se.tornroth.kodi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import se.tornroth.kodi.entity.Player;

public class KodiPlayerService extends AbstractKodiService {

	private final static List<String> SERVERS = Arrays.asList("http://192.168.1.204:80/jsonrpc",
			"http://192.168.1.205:80/jsonrpc");

	public List<String> pause() {
		List<Player> activePlayers = findActivePlayers(SERVERS);
		return activePlayers.stream().filter(player -> {
			return player.isPlaying();
		}).map(player -> {
			return sendPlayPause(player.getUrl(), player.getPlayerid());
		}).collect(Collectors.toList());
	}

	public List<String> resume() {
		List<Player> activePlayers = findActivePlayers(SERVERS);
		return activePlayers.stream().filter(player -> {
			return player.isPaused();
		}).map(player -> {
			return sendPlayPause(player.getUrl(), player.getPlayerid());
		}).collect(Collectors.toList());
	}

	public List<String> stop() {
		List<Player> activePlayers = findActivePlayers(SERVERS);
		return activePlayers.stream().map(player -> {
			return sendStop(player.getUrl(), player.getPlayerid());
		}).collect(Collectors.toList());
	}

	private List<Player> findActivePlayers(List<String> servers) {
		String activePlayersPaylod = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.GetActivePlayers\", \"id\": \"kodiService\"}";
		return servers.stream().map(server -> {
			String activePlayers = sendPost(server, activePlayersPaylod);

			Optional<String> playerid = findValueFromArray(activePlayers, "playerid");
			return new Player(server, playerid.orElse(null));
		}).filter(player -> player.hasPlayerId()).map(player -> {
			String playerStatusPayload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.GetProperties\","
					+ "\"params\": {\"playerid\": " + player.getPlayerid()
					+ "	, \"properties\": [\"speed\"]}, \"id\": \"kodiService\"}";
			String playerStatus = sendPost(player.getUrl(), playerStatusPayload);

			Optional<String> speed = findValue(playerStatus, "speed");
			return new Player(player.getUrl(), player.getPlayerid(), speed.orElse(null));
		}).collect(Collectors.toList());
	}

	private String sendPlayPause(String url, String playerid) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.PlayPause\", " + "\"params\": { \"playerid\": "
				+ playerid + "}, \"id\": \"kodiService\"}";
		String result = sendPost(url, payload);
		return result;
	}

	private String sendStop(String url, String playerid) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Stop\", " + "\"params\": { \"playerid\": "
				+ playerid + "}, \"id\": \"kodiService\"}";
		String result = sendPost(url, payload);
		return result;
	}
}
