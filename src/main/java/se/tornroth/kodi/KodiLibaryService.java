package se.tornroth.kodi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
}
