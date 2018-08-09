package se.tornroth.kodi.entity;

public class Player {
	private final Mediaplayer mediaplayer;
	private final String playerid;
	private final String speed;

	public Player(Mediaplayer mediaplayer, String playerid) {
		this.mediaplayer = mediaplayer;
		this.playerid = playerid;
		this.speed = null;
	}

	public Player(Mediaplayer mediaplayer, String playerid, String speed) {
		this.mediaplayer = mediaplayer;
		this.playerid = playerid;
		this.speed = speed;
	}

	public Mediaplayer getMediaplayer() {
		return mediaplayer;
	}

	public String getPlayerid() {
		return playerid;
	}

	public boolean hasPlayerId() {
		return playerid != null;
	}

	public String getSpeed() {
		return speed;
	}

	public boolean isPlaying() {
		return speed != null && Integer.parseInt(speed) > 0;
	}

	public boolean isPaused() {
		return speed != null && Integer.parseInt(speed) == 0;
	}

	@Override
	public String toString() {
		return "Player [mediaplayer=" + mediaplayer + ", playerid=" + playerid + ", speed=" + speed + "]";
	}

}
