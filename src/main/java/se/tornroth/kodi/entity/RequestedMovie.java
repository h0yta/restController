package se.tornroth.kodi.entity;

public class RequestedMovie {
	private final RequestType type;
	private final Mediaplayer mediaplayer;
	private final String title;
	private final String genre;

	public RequestedMovie(RequestType type, Mediaplayer mediaplayer, String title) {
		this.type = type;
		this.mediaplayer = mediaplayer;
		this.title = title;
		this.genre = null;
	}

	public RequestedMovie(RequestType type, Mediaplayer mediaplayer, String title, String genre) {
		this.type = type;
		this.mediaplayer = mediaplayer;
		this.title = title;
		this.genre = genre;
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

	public String getGenre() {
		return genre;
	}

	@Override
	public String toString() {
		return "RequestedMovie [type=" + type + ", mediaplayer=" + mediaplayer + ", title=" + title + ", genre=" + genre
				+ "]";
	}

}
