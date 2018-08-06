package se.tornroth.kodi.entity;

public class RequestedTvshow {
	private final Type type;
	private final Mediaplayer mediaplayer;
	private final String title;
	private final String season;
	private final String episode;
	private final String episodeTitle;

	public RequestedTvshow(Type type, Mediaplayer mediaplayer, String title) {
		this.type = type;
		this.mediaplayer = mediaplayer;
		this.title = title.trim();
		this.season = null;
		this.episode = null;
		this.episodeTitle = null;
	}

	public RequestedTvshow(Type type, Mediaplayer mediaplayer, String title, String season, String episode) {
		this.type = type;
		this.mediaplayer = mediaplayer;
		this.title = title.trim();
		this.season = season.trim();
		this.episode = episode.trim();
		this.episodeTitle = null;
	}

	public RequestedTvshow(Type type, Mediaplayer mediaplayer, String title, String episodeTitle) {
		this.type = type;
		this.mediaplayer = mediaplayer;
		this.title = title.trim();
		this.season = null;
		this.episode = null;
		this.episodeTitle = episodeTitle.trim();
	}

	public Type getType() {
		return type;
	}

	public Mediaplayer getMediaplayer() {
		return mediaplayer;
	}

	public String getTitle() {
		return title;
	}

	public String getSeason() {
		return season;
	}

	public String getEpisode() {
		return episode;
	}

	public String getEpisodeTitle() {
		return episodeTitle != null ? episodeTitle : "";
	}

	public String getEpisodeDescShort() {
		if (season != null && episode != null) {
			return this.season + "x" + String.format("%02d", Integer.parseInt(this.episode)).trim();
		}

		return "";
	}

	public enum Type {
		SPECIFIC_EPISODE, EPISODE_TITLE, LATEST_EPISODE, NEXT_EPISODE, RANDOM_EPISODE;
	}

	public enum Mediaplayer {
		BASEMENT, LIVINGROOM;
	}

	@Override
	public String toString() {
		return "RequestedTvshow [type=" + type + ", mediaplayer=" + mediaplayer + ", title=" + title + ", season="
				+ season + ", episode=" + episode + ", episodeTitle=" + episodeTitle + "]";
	}
}
