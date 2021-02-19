package com.server.parser.java.call;

import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CallHandlerTest {

    @Test
    void shouldBeSpecificEqualsMethod() {
        CallHandler handler = new CallHandler();
        CallReference callReference = new CallReference(mock(Variable.class), "equals");
        List<Expression> args = Collections.singletonList(mock(Expression.class));

        boolean result = handler.isSpecificEqualsMethod(new CallInvocation("", "", callReference, args));

        assertThat(result).isTrue();
    }
}