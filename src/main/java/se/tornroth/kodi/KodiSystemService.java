package se.tornroth.kodi;

import java.util.Optional;

import javax.inject.Inject;

import se.tornroth.http.HttpService;
import se.tornroth.kodi.entity.Mediaplayer;

public class KodiSystemService extends AbstractKodiService {

	@Inject
	private HttpService httpService;

	public Optional<String> rebootSystem(Mediaplayer mediaplayer) {
		return Optional.ofNullable(sendReboot(mediaplayer.getUrl()));
	}

	private String sendReboot(String url) {
		String payload = "{\"jsonrpc\": \"2.0\", \"method\": \"System.Reboot\", " + "\"id\": \"kodiService\"}";
		String result = httpService.sendPost(url, payload);
		return result;
	}
}
