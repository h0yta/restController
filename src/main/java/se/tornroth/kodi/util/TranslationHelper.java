package se.tornroth.kodi.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class TranslationHelper {

	private static final String TRANSLATIONS = "/var/flags/translations.json";
	private static Map<String, String> translatedTitles = new HashMap<>();

	public Optional<String> translateTitle(String title) {
		createTranslationsMapIfNotExists();

		if (translatedTitles.containsKey(title.toLowerCase())) {
			return Optional.of(translatedTitles.get(title.toLowerCase()));
		}

		return translatedTitles.keySet().stream().filter(translatedTitle -> {
			return KodiUtils.simularEnough(translatedTitle, title);
		}).findFirst();
	}

	public void addTranslation(String req, String translation) {
		createTranslationsMapIfNotExists();

		translatedTitles.put(req.toLowerCase(), translation.toLowerCase());
		writeTranslationsMap();
	}

	private void createTranslationsMapIfNotExists() {
		if (translatedTitles.isEmpty()) {
			readTranslationsMap();
		}
	}

	@SuppressWarnings("unchecked")
	private static void readTranslationsMap() {
		Gson gson = new Gson();
		try {
			translatedTitles = gson.fromJson(new FileReader(TRANSLATIONS), translatedTitles.getClass());
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			System.err.println("Unable to read from json");
		}
	}

	private static void writeTranslationsMap() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(translatedTitles);
		try {
			FileWriter writer = new FileWriter(TRANSLATIONS);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			System.err.println("Unable to write to json");
		}
	}
}
