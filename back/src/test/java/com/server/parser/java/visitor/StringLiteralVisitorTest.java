package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StringLiteralVisitorTest extends JavaVisitorTestBase {
    private final StringLiteralVisitor visitor = new StringLiteralVisitor();

    static Stream<Arguments> literalsProvider() {
        return Stream.of(
                Arguments.of("\"abc\"", "abc"),
                Arguments.of("\"a b\"", "a b"),
                Arguments.of("\"a\\\"a\"", "a\\\"a"),
                Arguments.of("'a'", "a")
        );
    }

    @ParameterizedTest
    @MethodSource("literalsProvider")
    void shouldVisitStringLiteral(String literal, String expected) {
        JavaParser.LiteralContext c = HELPER.shouldParseToEof(literal, JavaParser::literal);

        String visitedStringLiteral = visitor.visit(c);

        assertThat(visitedStringLiteral).isEqualTo(expected);
    }
}