package se.tornroth.kodi.service;

import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public abstract class AbstractKodiService {

	Optional<String> findValue(String json, String key) {
		JsonElement root = new JsonParser().parse(json);
		JsonElement value = root.getAsJsonObject().get("result").getAsJsonObject().get(key);
		if (value != null) {
			return Optional.of(value.getAsString());
		}

		return Optional.empty();
	}

	Optional<String> findValueFromArray(String json, String key) {
		JsonElement root = new JsonParser().parse(json);
		JsonArray resultArray = root.getAsJsonObject().get("result").getAsJsonArray();
		if (resultArray.size() > 0) {
			for (JsonElement elem : resultArray) {
				JsonElement type = elem.getAsJsonObject().get("type");
				JsonElement value = elem.getAsJsonObject().get(key);
				if (type != null && "video".equals(type.getAsString()) && value != null) {
					return Optional.of(value.getAsString());
				}
			}
		}
		return Optional.empty();
	}

}
