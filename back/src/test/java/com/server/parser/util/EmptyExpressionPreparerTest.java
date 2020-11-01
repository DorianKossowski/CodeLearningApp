package com.server.parser.util;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmptyExpressionPreparerTest {

    @Test
    void shouldPrepareForObjectType() {
        Expression expression = EmptyExpressionPreparer.prepare("String");

        assertThat(expression).isExactlyInstanceOf(NullExpression.class);
    }

    @Test
    void shouldPrepareForPrimitive() {
        Expression expression = EmptyExpressionPreparer.prepare("float");

        assertThat(expression).isExactlyInstanceOf(Literal.class);
        assertThat(expression.getConstant().c).isEqualTo(0.0);
    }
}