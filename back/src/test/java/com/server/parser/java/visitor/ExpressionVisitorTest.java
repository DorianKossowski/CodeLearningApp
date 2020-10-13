package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.ObjectRef;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionVisitorTest extends JavaVisitorTestBase {
    private final ExpressionVisitor visitor = new ExpressionVisitor();

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