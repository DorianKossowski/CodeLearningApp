package com.server.parser.java;

import java.util.List;

public class JavaGrammarHelper {

    public static String createMethodName(List<String> identifiers) {
        return String.join(".", identifiers);
    }
}