package com.server.parser.java;

import com.server.parser.ParserTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Test
    void shouldParseModifiers() {
        List<String> classModifiers = Arrays.asList("public", "protected", "private", "abstract", "static", "final",
                "strictfp");
        List<String> methodModifiers = Stream.concat(classModifiers.stream(), Stream.of("synchronized", "native"))
                .collect(Collectors.toList());

        classModifiers.forEach(modifier -> HELPER.shouldParseToEof(modifier, JavaParser::classModifier));
        methodModifiers.forEach(modifier -> HELPER.shouldParseToEof(modifier, JavaParser::methodModifier));
    }
}