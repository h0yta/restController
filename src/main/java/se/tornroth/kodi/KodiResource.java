package se.tornroth.kodi;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.google.gson.Gson;

@Path("kodi")
public class KodiResource {

	@Inject
	private KodiPlayerService kodiPlayerService;

	@Inject
	private KodiPlaylistService kodiPlaylistService;

	@Inject
	private KodiLibaryService kodiLibaryService;

	@Inject
	private KodiSystemService kodiSystemService;

	@PUT
	@Path("pause")
	public String pause() {
		List<String> result = kodiPlayerService.pause();
		return new Gson().toJson(result);
	}

	@PUT
	@Path("resume")
	public String resume() {
		List<String> result = kodiPlayerService.resume();
		return new Gson().toJson(result);
	}

	@PUT
	@Path("stop")
	public String stop() {
		List<String> result = kodiPlayerService.stop();
		return new Gson().toJson(result);
	}

	@PUT
	@Path("clear")
	public String clear() {
		List<String> result = kodiPlaylistService.clearPlaylist();
		return new Gson().toJson(result);
	}

	@PUT
	@Path("scan")
	public String scan() {
		List<String> result = kodiLibaryService.scan();
		return new Gson().toJson(result);
	}

	@PUT
	@Path("clean")
	public String clean() {
		List<String> result = kodiLibaryService.clean();
		return new Gson().toJson(result);
	}

	@PUT
	@Path("reboot")
	public String reboot() {
		List<String> result = kodiSystemService.reboot();
		return new Gson().toJson(result);
	}
}
