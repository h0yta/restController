package se.tornroth.kodi.entity;

public class Playlist {
	private final Mediaplayer mediaplayer;
	private final String playlistid;

	public Playlist(Mediaplayer mediaplayer, String playerid) {
		this.mediaplayer = mediaplayer;
		this.playlistid = playerid;
	}

	public Mediaplayer getMediaplayer() {
		return mediaplayer;
	}

	public String getPlaylistid() {
		return playlistid;
	}

	public boolean hasPlaylistId() {
		return playlistid != null;
	}

	@Override
	public String toString() {
		return "Playlist [url=" + mediaplayer + ", playlistid=" + playlistid + "]";
	}

}
