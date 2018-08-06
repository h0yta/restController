package se.tornroth.kodi.entity;

public class Tvshow {
	private final String url;
	private final String title;
	private final Integer id;

	public Tvshow(String url, String title, Integer id) {
		this.url = url;
		this.title = title;
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public Integer getId() {
		return id;
	}
}
