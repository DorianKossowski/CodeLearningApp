package com.server.parser.java.call;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.executor.MethodCallExecutor;
import com.server.parser.java.call.executor.StaticCallExecutor;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.PrintCallReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CallResolverTest {
    @Mock
    private CallableKeeper callableKeeper;
    @Mock
    private MethodCallExecutor methodCallExecutor;
    @Mock
    private StaticCallExecutor staticCallExecutor;

    private CallResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new CallResolver(callableKeeper, methodCallExecutor, staticCallExecutor);
    }

    @Test
    void shouldBeSpecificEqualsMethod() {
        CallReference callReference = new CallReference(mock(Variable.class), "equals");
        List<Expression> args = Collections.singletonList(mock(Expression.class));

        boolean result = resolver.isSpecificEqualsMethod(new CallInvocation("", "", callReference, args));

        assertThat(result).isTrue();
    }

    @Test
    void shouldKeepPrintCalls() {
        CallInvocation invocation = createSimplePrintCall();
        when(staticCallExecutor.executePrintMethod(invocation)).thenReturn(new PrintCallStatement(invocation));

        resolver.resolve(invocation);

        assertThat(Iterables.getOnlyElement(resolver.getResolvedPrintCalls()).getCallInvocation()).isSameAs(invocation);
    }

    private CallInvocation createSimplePrintCall() {
        return new CallInvocation("", "", new PrintCallReference("System.out.print"), Collections.emptyList());
    }
}