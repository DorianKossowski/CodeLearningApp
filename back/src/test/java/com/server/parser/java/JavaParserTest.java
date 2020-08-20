package com.server.parser.java;

import com.server.parser.ParserTestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaParserTest extends ParserTestBase<JavaParser> {

    public JavaParserTest() {
        super(JavaLexer::new, JavaParser::new);
    }

    @ParameterizedTest
    @ValueSource(strings = {"_", "$", "a", "$a1", "A1_"})
    void shouldParseIdentifier(String input) {
        shouldParseToEof(input, JavaParser::identifier);
    }

    @Test
    void shouldParseClassDec() {
        String input = "class Hello { body }";

        shouldParseToEof(input, JavaParser::classDec);
    }
}