package com.server.parser.java.call.executor;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.value.ObjectValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.visitor.StatementListVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MethodCallExecutorTest {
    @Mock
    private StatementListVisitor visitor;

    private MethodCallExecutor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new MethodCallExecutor(visitor);
    }

    @Test
    void shouldExecuteSpecialEqualsMethod() {
        ObjectValue value = mock(ObjectValue.class);
        ObjectValue expressionValue = mock(ObjectValue.class);
        when(value.equalsMethod(expressionValue)).thenReturn(true);
        Expression expression = mockExpression(expressionValue);
        CallReference callReference = new CallReference(value, "equals");
        CallInvocation invocation = new CallInvocation("", "", callReference, Collections.singletonList(expression));

        CallStatement statement = executor.executeSpecialEqualsMethod(invocation);

        assertThat(statement.getCallInvocation()).isSameAs(invocation);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isSameAs(invocation);
        assertThat(statement.getResult().getResolvedText()).isEqualTo("true");
    }


    private Expression mockExpression(Value expressionValue) {
        Expression expression = mock(Expression.class);
        when(expression.getValue()).thenReturn(expressionValue);
        return expression;
    }
}