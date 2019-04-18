package se.tornroth.kodi.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class EpisodeLengthHelper {

	private static final String EPISODE_LENGTH = "/var/flags/episodelength.json";
	private static final Double DEFAULT_LENGTH = 12d;
	private static Map<String, Double> episodeLengths = new HashMap<>();

	public Double findEpisodeLength(String title) {
		createMapIfNotExists();

		if (episodeLengths.containsKey(title.toLowerCase())) {
			return episodeLengths.get(title.toLowerCase());
		}

		return DEFAULT_LENGTH;
	}

	public void addEpisodeLength(String req, Double episodeLength) {
		createMapIfNotExists();

		episodeLengths.put(req.toLowerCase(), episodeLength);
		writeMap();
	}

	private void createMapIfNotExists() {
		if (episodeLengths.isEmpty()) {
			readMap();
		}
	}

	@SuppressWarnings("unchecked")
	private static void readMap() {
		Gson gson = new Gson();
		try {
			episodeLengths = gson.fromJson(new FileReader(EPISODE_LENGTH), episodeLengths.getClass());
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			System.err.println("Unable to read from json");
		}
	}

	private static void writeMap() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(episodeLengths);
		try {
			FileWriter writer = new FileWriter(EPISODE_LENGTH);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			System.err.println("Unable to write to json");
		}
	}
}
