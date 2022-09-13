package com.nix;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws IOException {
        ArrayList<String> keys = new ArrayList<>();
        Map<String, Object> jsonElements = new ObjectMapper().readValue(new File("src/main/resources/request.json"), new TypeReference<>() {
        });

        Map<String, String> types = new LinkedHashMap<>();

        getAllKeys(jsonElements, keys, types);

        for (String key : keys) {
            System.out.println(key + "," + types.get(key));
        }
    }

    private static void getAllKeys(Map<String, Object> jsonElements, List<String> keys, Map<String, String> types) {

        jsonElements.forEach((key, value) -> {
            keys.add(key);
            if (!(value instanceof List)) {
                if (value == null) {
                    types.put(key, "null");
                } else {
                    types.put(key, String.valueOf(value.getClass()));
                }
            }
            if (value instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) value;
                getAllKeys(map, keys, types);
            } else if (value instanceof List) {
                List<?> list = (List<?>) value;
                list.forEach(listEntry -> {
                    if (listEntry instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) listEntry;
                        getAllKeys(map, keys, types);
                    }
                });
            }
        });
    }

}
