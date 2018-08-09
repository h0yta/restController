package se.tornroth.kodi.entity;

public class Tvshow {
	private final Mediaplayer mediaplayer;
	private final String title;
	private final Integer id;

	public Tvshow(Mediaplayer mediaplayer, String title, Integer id) {
		this.mediaplayer = mediaplayer;
		this.title = title;
		this.id = id;
	}

	public Mediaplayer getMediaplayer() {
		return mediaplayer;
	}

	public String getTitle() {
		return title;
	}

	public Integer getId() {
		return id;
	}
}
