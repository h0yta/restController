package se.tornroth.kodi.entity;

public enum Mediaplayer {
	BASEMENT("http://192.168.1.221:80/jsonrpc"),

	LIVINGROOM("http://192.168.1.222:80/jsonrpc"),

	GYM("http://192.168.1.223:80/jsonrpc");

	private final String url;

	private Mediaplayer(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
