package se.tornroth.kodi.entity;

public class Movie {
	private final Mediaplayer mediaplayer;
	private final Integer movieId;
	private final String title;
	private final boolean watched;

	public Movie(Mediaplayer mediaplayer, Integer movieId, String title, boolean watched) {
		this.mediaplayer = mediaplayer;
		this.movieId = movieId;
		this.title = title;
		this.watched = watched;
	}

	public Mediaplayer getMediaplayer() {
		return mediaplayer;
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
		return "Movie [mediaplayer=" + mediaplayer + ", movieId=" + movieId + ", title=" + title + ", watched="
				+ watched + "]";
	}

}
