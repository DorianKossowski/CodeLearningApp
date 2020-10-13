package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.ObjectRef;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionVisitorTest extends JavaVisitorTestBase {
    private final ExpressionVisitor visitor = new ExpressionVisitor();

    static Stream<Arguments> literalsProvider() {
        return Stream.of(
                Arguments.of("\"abc\"", "\"abc\"", String.class),
                Arguments.of("\"a b\"", "\"a b\"", String.class),
                Arguments.of("\"a\\\"a\"", "\"a\"a\"", String.class),
                Arguments.of("'a'", "'a'", Character.class),
                Arguments.of("5", "5", Integer.class),
                Arguments.of("5l", "5", Integer.class),
                Arguments.of("5L", "5", Integer.class),
                Arguments.of("5.5f", "5.5", Double.class),
                Arguments.of("0.5f", "0.5", Double.class)
        );
    }

    @ParameterizedTest
    @MethodSource("literalsProvider")
    void shouldVisitLiteral(String input, String expected, Class<?> classLiteral) {
        JavaParser.LiteralContext c = HELPER.shouldParseToEof(input, JavaParser::literal);

        Literal literal = (Literal) visitor.visit(c, context);

        assertThat(literal.getValue()).isExactlyInstanceOf(classLiteral);
        assertThat(literal.getText()).isEqualTo(expected);
    }

    @Test
    void shouldVisitLiteralExpression() {
        String input = "\"base string literal\"";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        Expression expression = visitor.visit(c, context);

        assertThat(expression).isExactlyInstanceOf(Literal.class);
        assertThat(expression.getText()).isEqualTo(input);
    }

    @Test
    void shouldVisitObjectRefExpression() {
        MethodContext methodContext = new MethodContext("");
        methodContext.addVar("x", "value");
        context.putMethodWithContext(methodContext);
        String input = "x";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        Expression expression = visitor.visit(c, context);

        assertThat(expression).isExactlyInstanceOf(ObjectRef.class);
        assertThat(expression.getText()).isEqualTo("x");
        assertThat(expression.getResolved()).isEqualTo("value");
    }

    @Test
    void shouldThrowWhenWrongObjectRefExpression() {
        context.putMethodWithContext(new MethodContext(""));
        String input = "x";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        assertThatThrownBy(() -> visitor.visit(c, context))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt x nie istnieje");
    }
}