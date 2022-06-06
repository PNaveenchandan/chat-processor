package com.smarttech.agribot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smarttech.agribot.config.GoogleConfig;
import com.smarttech.agribot.dto.GoogleTranslateRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Component
public class GoogleTranslator {

	public static String translate(GoogleConfig config, String text, String target) throws JsonProcessingException {
		GoogleTranslateRequest googleTranslateRequestDTO = new GoogleTranslateRequest();
		googleTranslateRequestDTO.setQ(Arrays.asList(text));
		googleTranslateRequestDTO.setTarget(target);

		ObjectMapper mapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> httpHeaders = new HashMap<>();
		httpHeaders.put("Content-Type", "application/json; charset=utf-8");
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("key", config.getApiKey());

		HttpHeaders headers = new HttpHeaders();
		for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
			headers.set(entry.getKey(), entry.getValue());
		}
		String requestBody = mapper.writeValueAsString(googleTranslateRequestDTO);
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		URI parameterizedUri = getParameterizedUri(queryParams, config.getTranslateURL());
		ResponseEntity<String> responseEntity = restTemplate.exchange(parameterizedUri, HttpMethod.POST, httpEntity,
			String.class);
		JsonObject response =  new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
		System.out.println(response);
		return response.get("data").getAsJsonObject().get("translations").getAsJsonArray().get(0).getAsJsonObject().get("translatedText").getAsString();
	}


	private static URI getParameterizedUri(Map<String, String> postParams, String url) {
		URI uri = null;
		try {
			if (postParams != null && !postParams.isEmpty()) {
				URIBuilder builder = null;
				builder = new URIBuilder(url);
				List<NameValuePair> result = new ArrayList<NameValuePair>(postParams.size());
				for (String key : postParams.keySet()) {
					result.add(new BasicNameValuePair(key, postParams.get(key)));
				}
				builder.setParameters(result);
				uri = builder.build();
			} else {
				uri = new URI(url);
			}
		} catch (URISyntaxException ex) {
		}
		return uri;
	}
}
