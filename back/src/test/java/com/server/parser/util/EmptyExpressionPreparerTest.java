package com.server.parser.util;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.java.value.UninitializedValue;
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
        assertThat(expression.getLiteral().getConstant().c).isEqualTo(0.0);
    }

    @Test
    void shouldPrepareUninitialized() {
        Expression expression = EmptyExpressionPreparer.prepareUninitialized("id");

        assertThat(expression).isExactlyInstanceOf(UninitializedExpression.class);
        assertThat(expression.getText()).isEqualTo("id");
        assertThat(expression.getValue()).isExactlyInstanceOf(UninitializedValue.class);
    }
}