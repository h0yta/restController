package se.tornroth.kodi;

import org.testng.Assert;
import org.testng.annotations.Test;

public class KodiUtilsTest {

	@Test
	public void simularEnough() {
		Assert.assertTrue(KodiUtils.simularEnough("grizzly and the Lemmings", "Grizzy & the lemmings"));
	}

	@Test(enabled = false)
	public void calculateSimularity() {
		Assert.assertEquals(KodiUtils.calculateSimularity("grizzly and the Lemmings", "Grizzy & the lemmings"), 0.85d);
	}
}
