package se.tornroth.kodi.entity;

public class Playlist {
	private final String url;
	private final String playlistid;
	
	public Playlist(String url, String playerid) {
		this.url = url;
		this.playlistid = playerid;
	}

	public String getUrl() {
		return url;
	}

	public String getPlaylistid() {
		return playlistid;
	}

	public boolean hasPlaylistId() {
		return playlistid != null;
	}

	@Override
	public String toString() {
		return "Playlist [url=" + url + ", playlistid=" + playlistid + "]";
	}

}
