package se.tornroth.kodi;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.Gson;

import se.tornroth.kodi.entity.Mediaplayer;

@Path("kodi")
public class KodiResource {

	@Inject
	private KodiService kodiService;

	@PUT
	@Path("pause/{location}")
	public String pause(@PathParam("location") String location) {
		Optional<String> result = kodiService.pausePlayer(findMediaplayer(location));
		return new Gson().toJson(createResponse(result));
	}

	@PUT
	@Path("resume/{location}")
	public String resume(@PathParam("location") String location) {
		Optional<String> result = kodiService.resumePlayer(findMediaplayer(location));
		return new Gson().toJson(createResponse(result));
	}

	@PUT
	@Path("stop/{location}")
	public String stop(@PathParam("location") String location) {
		Optional<String> result = kodiService.stopPlayer(findMediaplayer(location));
		return new Gson().toJson(createResponse(result));
	}

	@POST
	@Path("play/{location}/{item}")
	public String playEpisde(@PathParam("location") String location, @PathParam("item") String request) {
		System.out.println(location + " -> " + request);
		Optional<String> result = kodiService.play(findMediaplayer(location), request);
		return new Gson().toJson(createResponse(result));
	}

	@PUT
	@Path("clear")
	public String clear(@PathParam("location") String location) {
		Optional<String> result = kodiService.clearPlaylist(findMediaplayer(location));
		return new Gson().toJson(createResponse(result));
	}

	@PUT
	@Path("scan")
	public String scan() {
		Optional<String> result = kodiService.scanLibrary();
		return new Gson().toJson(createResponse(result));
	}

	@PUT
	@Path("clean")
	public String clean() {
		Optional<String> result = kodiService.cleanLibrary();
		return new Gson().toJson(createResponse(result));
	}

	@PUT
	@Path("reboot/{location}")
	public String reboot(@PathParam("location") String location) {
		Optional<String> result = kodiService.rebootSystem(findMediaplayer(location));
		return new Gson().toJson(createResponse(result));
	}

	private Mediaplayer findMediaplayer(String player) {
		return Mediaplayer.valueOf(player);
	}

	private String createResponse(Optional<String> result) {
		if (result.isPresent()) {
			return result.get();
		}

		return "Error";
	}
}
