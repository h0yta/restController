package se.tornroth.kodi;

import java.util.Optional;

import javax.inject.Inject;

import se.tornroth.kodi.entity.Mediaplayer;
import se.tornroth.kodi.entity.Request;

public class KodiService {

	@Inject
	private KodiPlayerService kodiPlayerService;

	@Inject
	private KodiPlaylistService kodiPlaylistService;

	@Inject
	private KodiLibaryService kodiLibaryService;

	@Inject
	private KodiSystemService kodiSystemService;

	@Inject
	private RequestService requestService;

	public Optional<String> pausePlayer(Mediaplayer mediaplayer) {
		return kodiPlayerService.pausePlayer(mediaplayer);
	}

	public Optional<String> resumePlayer(Mediaplayer mediaplayer) {
		return kodiPlayerService.resumePlayer(mediaplayer);
	}

	public Optional<String> stopPlayer(Mediaplayer mediaplayer) {
		return kodiPlayerService.stopPlayer(mediaplayer);
	}

	public Optional<String> play(Mediaplayer mediaplayer, String requestString) {
		Request request = requestService.createRequest(mediaplayer, requestString);
		return kodiPlayerService.play(request);
	}

	public Optional<String> clearPlaylist(Mediaplayer mediaplayer) {
		return kodiPlaylistService.clearPlaylist(mediaplayer);
	}

	public Optional<String> scanLibrary() {
		return kodiLibaryService.scanLibrary(Mediaplayer.BASEMENT);
	}

	public Optional<String> cleanLibrary() {
		return kodiLibaryService.cleanLibrary(Mediaplayer.BASEMENT);
	}

	public Optional<String> rebootSystem(Mediaplayer mediaplayer) {
		return kodiSystemService.rebootSystem(mediaplayer);
	}
}
