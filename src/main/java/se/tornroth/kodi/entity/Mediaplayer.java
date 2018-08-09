package se.tornroth.kodi.entity;

public enum Mediaplayer {
	BASEMENT("http://192.168.1.204:80/jsonrpc"), LIVINGROOM("http://192.168.1.205:80/jsonrpc");

	private final String url;

	private Mediaplayer(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
