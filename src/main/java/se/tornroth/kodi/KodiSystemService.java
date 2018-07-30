package se.tornroth.kodi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KodiSystemService extends AbstractKodiService {

	private final static List<String> SERVERS = Arrays.asList("http://192.168.1.204:80/jsonrpc",
			"http://192.168.1.205:80/jsonrpc");

	public List<String> reboot() {
		return SERVERS.stream().map(server -> {
			return sendReboot(server);
		}).collect(Collectors.toList());
	}

	private String sendReboot(String url) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"System.Reboot\", " + "\"id\": \"kodiService\"}";
		String result = sendPost(url, payload);
		return result;
	}
}
