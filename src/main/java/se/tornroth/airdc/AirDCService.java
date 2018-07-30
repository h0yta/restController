package se.tornroth.airdc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.tornroth.airdc.entity.Download;

public class AirDCService {

	public void download(String request) {
		if (requestIsSeries(request)) {
			downloadSeries(request);
		} else {
			downloadMovie(request);
		}
	}

	private boolean requestIsSeries(String request) {
		return request.contains("season") && request.contains("episode");
	}

	private void downloadMovie(String request) {

	}

	private void downloadSeries(String request) {

	}

	protected static Download getSeriesDownload(String request) {

		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request);
		while (matcher.find()) {
			return new Download(matcher.group(1), matcher.group(2), matcher.group(3));
		}

		throw new IllegalArgumentException("Invalid request string");
	}
}
