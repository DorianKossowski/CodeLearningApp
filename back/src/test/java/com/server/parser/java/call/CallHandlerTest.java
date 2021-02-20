package com.server.parser.java.call;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CallHandlerTest {
    @Mock
    private CallableKeeper callableKeeper;
    @Mock
    private CallExecutor callExecutor;

    private CallHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new CallHandler(callableKeeper, callExecutor);
    }

    @Test
    void shouldBeSpecificEqualsMethod() {
        CallReference callReference = new CallReference(mock(Variable.class), "equals");
        List<Expression> args = Collections.singletonList(mock(Expression.class));

        boolean result = handler.isSpecificEqualsMethod(new CallInvocation("", "", callReference, args));

        assertThat(result).isTrue();
    }

    @Test
    void shouldKeepPrintCalls() {
        CallInvocation invocation = createSimplePrintCall();
        when(callExecutor.executePrintMethod(invocation)).thenReturn(new CallStatement(invocation, Collections.emptyList()));
        handler.execute(invocation);

        assertThat(Iterables.getOnlyElement(handler.getPrintCalls()).getCallInvocation()).isSameAs(invocation);
    }

    private CallInvocation createSimplePrintCall() {
        return new CallInvocation("", "", new CallReference("System.out.print"), Collections.emptyList());
    }
}