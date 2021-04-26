package com.server.parser.java.ast.statement.property;

import java.util.HashMap;
import java.util.Map;

public class PropertiesProvider {
    private final Map<String, String> properties = new HashMap<>();

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
}