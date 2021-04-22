package com.server.parser.java.call.executor;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MethodCallExecutorTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Method method;

    private MethodCallExecutor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new MethodCallExecutor();
        when(method.getHeader().getResult()).thenReturn("void");
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

    @Test
    void shouldExecuteMethod() {
        // given
        MethodCallExecutor spyExecutor = spy(executor);
        ObjectValue value = mock(ObjectValue.class);
        CallReference callReference = new CallReference(value, "equals");
        CallInvocation invocation = new CallInvocation("", "", callReference, Collections.emptyList());

        JavaContext executionContext = mock(JavaContext.class);
        doReturn(executionContext).when(spyExecutor).createExecutionContext(method, value);
        doReturn(Collections.emptyList()).when(spyExecutor).executeInContext(method, invocation, executionContext);

        // when
        CallStatement statement = spyExecutor.execute(method, invocation);

        // then
        assertThat(statement.getCallInvocation()).isSameAs(invocation);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isSameAs(invocation);
        assertThat(statement.getResult()).isSameAs(VoidExpression.INSTANCE);
    }

    @Test
    void shouldThrowWhenEmptyReference() {
        CallInvocation invocation = mock(CallInvocation.class, RETURNS_DEEP_STUBS);
        when(invocation.getCallReference().getValue()).thenReturn(Optional.empty());
        when(invocation.getText()).thenReturn("CALL()");

        assertThatThrownBy(() -> executor.getThisValue(invocation))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Niepoprawna próba wywołania: CALL()");
    }
}