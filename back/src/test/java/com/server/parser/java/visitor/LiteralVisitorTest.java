package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Literal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LiteralVisitorTest extends JavaVisitorTestBase {
    private final LiteralVisitor visitor = new LiteralVisitor();

    static Stream<Arguments> literalsProvider() {
        return Stream.of(
                Arguments.of("\"abc\"", "\"abc\"", String.class),
                Arguments.of("\"a b\"", "\"a b\"", String.class),
                Arguments.of("\"a\\\"a\"", "\"a\"a\"", String.class),
                Arguments.of("'a'", "'a'", Character.class),
                Arguments.of("5", "5", Integer.class)
        );
    }

    @ParameterizedTest
    @MethodSource("literalsProvider")
    void shouldVisitLiteral(String input, String expected, Class<?> classLiteral) {
        JavaParser.LiteralContext c = HELPER.shouldParseToEof(input, JavaParser::literal);

        Literal literal = visitor.visit(c);

        assertThat(literal.getValue()).isExactlyInstanceOf(classLiteral);
        assertThat(literal.getText()).isEqualTo(expected);
    }
}