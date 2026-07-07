package com.automation.testing.utile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperJsonObjectUtile {

	public static Map<String, String> readJsonByPath(String path) {
		Map<String, String> map = new HashMap<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			map = objectMapper.readValue(new File(path), new TypeReference<Map<String, String>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	


}
