package se.tornroth.kodi.entity;

public class RequestedMovie {
	private final RequestType type;
	private final Mediaplayer mediaplayer;
	private final String title;

	public RequestedMovie(RequestType type, Mediaplayer mediaplayer, String title) {
		this.type = type;
		this.mediaplayer = mediaplayer;
		this.title = title;
	}

	public RequestType getType() {
		return type;
	}

	public Mediaplayer getMediaplayer() {
		return mediaplayer;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "RequestedMovie [type=" + type + ", mediaplayer=" + mediaplayer + ", title=" + title + "]";
	}

}
