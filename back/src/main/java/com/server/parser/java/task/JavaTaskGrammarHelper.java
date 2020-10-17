package com.server.parser.java.task;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaTaskParser;

import java.util.Optional;

public class JavaTaskGrammarHelper {

    public static Optional<String> extractValue(JavaTaskParser.ValueOrEmptyContext context) {
        if (context.EMPTY() != null) {
            return Optional.empty();
        }
        return Optional.of(JavaGrammarHelper.getFromStringLiteral(context.STRING_LITERAL().getText()));
    }

    public static String getFromStringLiteral(String literal) {
        literal = literal.replaceAll("\\\\\"", "\"");
        return literal.substring(1, literal.length() - 1);
    }
}