package se.tornroth.kodi;

import org.testng.Assert;
import org.testng.annotations.Test;

public class KodiUtilsTest {

	@Test
	public void calculateSimularity() {
		Assert.assertTrue(KodiUtils.simularEnough("grizzly and the Lemmings", "Grizzy & the lemmings"));
	}
}
