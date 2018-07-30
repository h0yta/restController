package se.tornroth.airdc;

import org.testng.Assert;
import org.testng.annotations.Test;

import se.tornroth.airdc.entity.Download;

public class AirDCResourceTest {

	@Test
	public void getSeriesDownload() {
		Download result = AirDCService.getSeriesDownload("Stranger things season 1 episode 1");

		Assert.assertEquals(result.getTitle(), "Stranger things");
		Assert.assertEquals(result.getSeason(), "1");
		Assert.assertEquals(result.getEpisode(), "1");
	}
}
