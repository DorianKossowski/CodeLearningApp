package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionVisitorTest extends JavaVisitorTestBase {
    private final ExpressionVisitor visitor = new ExpressionVisitor();

    @Test
    void shouldVisitExpression() {
        String input = "\"base string literal\"";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        Expression expression = visitor.visit(c);

        assertThat(expression.getText()).isEqualTo(input);
    }
}