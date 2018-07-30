package se.tornroth.kodi;

import java.io.IOException;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public abstract class AbstractKodiService {

	String sendPost(String url, String payload) {
		StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(url);
		request.setEntity(entity);

		try {
			HttpResponse response = httpClient.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

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
