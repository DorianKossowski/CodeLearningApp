package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodVar;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.constant.StringConstant;
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
                Arguments.of("\"a\\\"a\"", "\"a\\\"a\"", String.class),
                Arguments.of("'a'", "'a'", Character.class),
                Arguments.of("5", "5", Integer.class),
                Arguments.of("5l", "5", Integer.class),
                Arguments.of("5L", "5", Integer.class),
                Arguments.of("5.5f", "5.5", Double.class),
                Arguments.of("0.5f", "0.5", Double.class),
                Arguments.of("true", "true", Boolean.class),
                Arguments.of("false", "false", Boolean.class)
        );
    }

    @ParameterizedTest
    @MethodSource("literalsProvider")
    void shouldVisitLiteral(String input, String expected, Class<?> classLiteral) {
        JavaParser.LiteralContext c = HELPER.shouldParseToEof(input, JavaParser::literal);

        Literal literal = (Literal) visitor.visit(c, context);

        assertThat(literal.getConstant().c).isExactlyInstanceOf(classLiteral);
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
    void shouldThrowWhenInvalidExpressionType() {
        MethodContext methodContext = createMethodContext();
        methodContext.addVariable(createStringVariable("x"));
        String input = "-x";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        assertThatThrownBy(() -> visitor.visit(c, methodContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Operacja niedostępna dla typu String");
    }

    private Variable createStringVariable(String name) {
        StringConstant stringConstant = new StringConstant("value");
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(stringConstant));
        return new MethodVar("String", name, value);
    }

    @Test
    void shouldVisitEqualsCall() {
        MethodContext methodContext = createMethodContext();
        methodContext.addVariable(createStringVariable("x"));
        methodContext.addVariable(createStringVariable("x2"));
        String input = "x.equals(x2)";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        Expression expression = visitor.visit(c, methodContext);

        assertThat(expression).isExactlyInstanceOf(Literal.class);
        assertThat(expression.getText()).isEqualTo("true");
        assertThat(expression.getLiteral().getConstant().c).isEqualTo(true);
    }

    @Test
    void shouldVisitAnd() {
        String input = "true && false";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        Expression expression = visitor.visit(c, context);

        assertThat(expression).isExactlyInstanceOf(Literal.class);
        assertThat(expression.getText()).isEqualTo("false");
        assertThat(expression.getLiteral().getConstant().c).isEqualTo(false);
    }

    @Test
    void shouldVisitOr() {
        String input = "true || false";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        Expression expression = visitor.visit(c, context);

        assertThat(expression).isExactlyInstanceOf(Literal.class);
        assertThat(expression.getText()).isEqualTo("true");
        assertThat(expression.getLiteral().getConstant().c).isEqualTo(true);
    }

    @Test
    void shouldVisitNull() {
        String input = "null";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        Expression expression = visitor.visit(c, context);

        assertThat(expression).isExactlyInstanceOf(NullExpression.class);
        assertThat(expression.getText()).isEqualTo("null");
    }
}