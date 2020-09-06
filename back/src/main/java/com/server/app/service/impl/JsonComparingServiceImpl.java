package com.server.app.service.impl;

import com.server.app.service.JsonComparingService;
import org.springframework.stereotype.Service;

@Service
public class JsonComparingServiceImpl implements JsonComparingService {
    private static final String TO_REMOVE_REGEX = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";

    @Override
    public boolean areEqual(String json1, String json2) {
        return removeWhiteSpaces(json1).equals(removeWhiteSpaces(json2));
    }

    String removeWhiteSpaces(String input) {
        return input.replaceAll(TO_REMOVE_REGEX, "");
    }
}