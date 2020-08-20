package com.server.parser.java;

import com.server.parser.ParserTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaParserTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @ParameterizedTest
    @ValueSource(strings = {"_", "$", "a", "$a1", "A1_"})
    void shouldParseIdentifier(String input) {
        HELPER.shouldParseToEof(input, JavaParser::identifier);
    }

    @Test
    void shouldParseClassDec() {
        String input = "class Hello { body }";

        HELPER.shouldParseToEof(input, JavaParser::classDec);
    }
}