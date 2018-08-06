package se.tornroth.kodi.entity;

public class Episode {
	private final String url;
	private final Integer tvshowId;
	private final Integer episodeId;
	private final Integer season;
	private final Integer episode;
	private final String title;
	private final boolean watched;

	public Episode(String url, Integer tvshowid, Integer episodeId, Integer season, Integer episode, String title,
			boolean watched) {
		this.url = url;
		this.tvshowId = tvshowid;
		this.episodeId = episodeId;
		this.title = title;
		this.season = season;
		this.episode = episode;
		this.watched = watched;
	}

	public String getUrl() {
		return url;
	}

	public Integer getTvshowId() {
		return tvshowId;
	}

	public Integer getEpisodeId() {
		return episodeId;
	}

	public Integer getSeason() {
		return season;
	}

	public Integer getEpisode() {
		return episode;
	}

	public String getTitle() {
		return title;
	}

	public boolean isWatched() {
		return watched;
	}

	public String findSeasonAndEpisode() {
		return title.substring(0, title.indexOf(".")).trim();
	}

	public String findEpisodeTitle() {
		return title.substring(title.indexOf(".") + 1, title.length()).trim();
	}

	@Override
	public String toString() {
		return "Episode [url=" + url + ", tvshowId=" + tvshowId + ", episodeId=" + episodeId + ", season=" + season
				+ ", episode=" + episode + ", title=" + title + ", watched=" + watched + "]";
	}

}
