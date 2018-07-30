package se.tornroth.kodi.entity;

public class Player {
	private final String url;
	private final String playerid;
	private final String speed;
	
	public Player(String url, String playerid) {
		this.url = url;
		this.playerid = playerid;
		this.speed = null;
	}

	public Player(String url, String playerid, String speed) {
		this.url = url;
		this.playerid = playerid;
		this.speed = speed;
	}

	public String getUrl() {
		return url;
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
		return "Player [url=" + url + ", playerid=" + playerid + ", speed=" + speed + "]";
	}

}
