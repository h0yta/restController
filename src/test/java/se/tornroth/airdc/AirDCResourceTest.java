package se.tornroth.airdc;

import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.Test;

import se.tornroth.airdc.entity.Download;

public class AirDCResourceTest {

	@Test
	public void requestIsSingleEpisode() {
		assertTrue(AirDCService.requestIsSingleEpisode("Stranger things season 1 episode 1"));
		Assert.assertFalse(AirDCService.requestIsSingleEpisode("Stranger things season 1 episode 1 until episode 4"));

	}

	@Test
	public void requestIsMultipleEpisode() {
		Assert.assertTrue(AirDCService.requestIsMultipleEpisode("Stranger things season 1 episode 1 until episode 4"));
		Assert.assertFalse(AirDCService.requestIsMultipleEpisode("Stranger things season 1 episode 1"));
	}

	@Test
	public void getSingleEpisodeDownload() {
		Download result = AirDCService.getSingleEpisodeDownload("Stranger things season 1 episode 1");

		Assert.assertEquals(result.getTitle(), "Stranger things");
		Assert.assertEquals(result.getSeason(), "1");
		Assert.assertEquals(result.getFromEpisode(), "1");
		Assert.assertNull(result.getToEpisode());
		Assert.assertEquals(result.getSearchString(), "Stranger things 1x1");
	}

	@Test
	public void getMultipleEpisodeDownload() {
		Download result = AirDCService.getMultipleEpisodeDownload("Stranger things season 1 episode 1 until episode 4");

		Assert.assertEquals(result.getTitle(), "Stranger things");
		Assert.assertEquals(result.getSeason(), "1");
		Assert.assertEquals(result.getFromEpisode(), "1");
		Assert.assertEquals(result.getToEpisode(), "4");
		Assert.assertEquals(result.getSearchString(), "Stranger things 1x1-4");
	}
}
