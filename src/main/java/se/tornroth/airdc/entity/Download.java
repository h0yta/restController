package se.tornroth.airdc.entity;

public class Download {
	private final String title;
	private final String season;
	private final String fromEpisode;
	private final String toEpisode;

	public Download(String title) {
		this.title = title;
		this.season = null;
		this.fromEpisode = null;
		toEpisode = null;
	}

	public Download(String title, String season, String episode) {
		this.title = title.trim();
		this.season = season.trim();
		this.fromEpisode = episode.trim();
		this.toEpisode = null;
	}

	public Download(String title, String season, String fromEpisode, String toEpisode) {
		this.title = title;
		this.season = season;
		this.fromEpisode = fromEpisode;
		this.toEpisode = toEpisode;
	}

	public String getTitle() {
		return title;
	}

	public String getSeason() {
		return season;
	}

	public String getFromEpisode() {
		return fromEpisode;
	}

	public String getToEpisode() {
		return toEpisode;
	}

	public String getSearchString() {
		if (season != null && fromEpisode != null && toEpisode != null) {
			return title + " " + season + "x" + fromEpisode + "-" + toEpisode;
		} else if (season != null && fromEpisode != null) {
			return title + " " + season + "x" + fromEpisode;
		}

		return title;
	}
}
