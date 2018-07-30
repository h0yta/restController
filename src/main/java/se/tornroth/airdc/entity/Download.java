package se.tornroth.airdc.entity;

public class Download {
	private final String title;
	private final String season;
	private final String episode;

	public Download(String title) {
		this.title = title;
		this.season = null;
		this.episode = null;
	}

	public Download(String title, String season, String episode) {
		super();
		this.title = title;
		this.season = season;
		this.episode = episode;
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

	public String getSearchString() {
		if (season != null && episode != null) {
			return title + " " + season + "x" + episode;
		}

		return title;
	}
}
