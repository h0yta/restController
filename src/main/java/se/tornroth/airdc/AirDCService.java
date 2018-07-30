package se.tornroth.airdc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.tornroth.airdc.entity.Download;

public class AirDCService {

	public void download(String request) {
		if (requestIsSingleEpisode(request)) {
			downloadSingleEpisode(request);
		} else if (requestIsMultipleEpisode(request)) {
			downloadMultipleEpisode(request);
		} else if (request.contains("season") && request.contains("episode")) {
			System.out.println("Invalid request: " + request);
		} else {
			downloadMovie(request);
		}
	}

	protected static boolean requestIsSingleEpisode(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request);
		return matcher.matches();
	}

	protected static boolean requestIsMultipleEpisode(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)\\s\\w+\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request);
		return matcher.matches();
	}

	private void downloadMovie(String request) {
		download(new Download(request));
	}

	private void downloadSingleEpisode(String request) {
		download(getSingleEpisodeDownload(request));
	}

	private void downloadMultipleEpisode(String request) {
		download(getMultipleEpisodeDownload(request));
	}

	protected static Download getSingleEpisodeDownload(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request);
		while (matcher.find()) {
			return new Download(matcher.group(1), matcher.group(2), matcher.group(3));
		}

		throw new IllegalArgumentException("Invalid request string");
	}

	protected static Download getMultipleEpisodeDownload(String request) {
		Pattern pattern = Pattern.compile("(.*)\\sseason\\s(\\d+)\\sepisode\\s(\\d+)\\s\\w+\\sepisode\\s(\\d+)");
		Matcher matcher = pattern.matcher(request);
		while (matcher.find()) {
			return new Download(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
		}

		throw new IllegalArgumentException("Invalid request string");
	}

	private void download(Download request) {
		String s = null;

		try {
			Process p = Runtime.getRuntime().exec(new String[] { "node", "/var/scripts/downloadManager/src/app.js",
					"-a", "download", "-r", request.getSearchString() });

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
