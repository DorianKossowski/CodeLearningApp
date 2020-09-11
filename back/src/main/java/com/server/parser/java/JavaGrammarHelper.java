package com.server.parser.java;

import java.util.List;

public class JavaGrammarHelper {

    public static String createMethodName(List<String> identifiers) {
        return String.join(".", identifiers);
    }

    public static String getFromStringLiteral(String literal) {
        literal = literal.replaceAll("\\\\\"", "\"");
        return literal.substring(1, literal.length() - 1);
    }
}