package se.tornroth.kodi.entity;

public class Movie {
	private final String url;
	private final Integer movieId;
	private final String title;
	private final boolean watched;

	public Movie(String url, Integer movieId, String title, boolean watched) {
		super();
		this.url = url;
		this.movieId = movieId;
		this.title = title;
		this.watched = watched;
	}

	public String getUrl() {
		return url;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public String getTitle() {
		return title;
	}

	public boolean isWatched() {
		return watched;
	}

	@Override
	public String toString() {
		return "Movie [url=" + url + ", movieId=" + movieId + ", title=" + title + ", watched=" + watched + "]";
	}

}
