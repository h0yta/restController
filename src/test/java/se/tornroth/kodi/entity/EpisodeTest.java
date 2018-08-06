package se.tornroth.kodi.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EpisodeTest {
	private Episode sut;

	@BeforeMethod
	public void beforeMethod() {
		sut = new Episode("", 1, 123, 2, 6, "2x06. Den flygande mattan: Del 1", false);
	}

	@Test
	public void findEpisodeTitle() {
		Assert.assertEquals(sut.findEpisodeTitle(), "Den flygande mattan: Del 1");
	}

	@Test
	public void findSeasonAndEpisode() {
		Assert.assertEquals(sut.findSeasonAndEpisode(), "2x06");
	}
}
