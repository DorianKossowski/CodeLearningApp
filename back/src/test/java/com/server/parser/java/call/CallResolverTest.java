package com.server.parser.java.call;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.executor.ConstructorCallExecutor;
import com.server.parser.java.call.executor.MethodCallExecutor;
import com.server.parser.java.call.executor.StaticCallExecutor;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.ConstructorCallReference;
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
    private ConstructorCallExecutor constructorCallExecutor;
    @Mock
    private MethodCallExecutor methodCallExecutor;
    @Mock
    private StaticCallExecutor staticCallExecutor;

    private CallResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new CallResolver(callableKeeper, constructorCallExecutor, methodCallExecutor, staticCallExecutor);
    }

    @Test
    void shouldBeSpecificEqualsMethod() {
        CallReference callReference = new CallReference(mock(Variable.class), "equals");
        List<Expression> args = Collections.singletonList(mock(Expression.class));

        boolean result = resolver.isSpecificEqualsMethod(new CallInvocation("", "", callReference, args));

        assertThat(result).isTrue();
    }

    @Test
    void shouldResolvePrintCall() {
        CallInvocation invocation = createSimpleCall(new PrintCallReference("System.out.print"));
        PrintCallStatement printCallStatement = new PrintCallStatement(invocation);
        when(staticCallExecutor.executePrintMethod(invocation)).thenReturn(printCallStatement);

        CallStatement resolvedStatement = resolver.resolve(invocation);

        assertThat(resolvedStatement).isSameAs(printCallStatement);
        assertThat(Iterables.getOnlyElement(resolver.getResolvedPrintCalls())).isSameAs(printCallStatement);
    }

    private CallInvocation createSimpleCall(CallReference reference) {
        return new CallInvocation("", "", reference, Collections.emptyList());
    }

    @Test
    void shouldResolveConstructorCall() {
        CallInvocation invocation = createSimpleCall(new ConstructorCallReference(""));
        Method method = mock(Method.class);
        when(callableKeeper.getCallable(invocation)).thenReturn(method);
        CallStatement statement = mock(CallStatement.class);
        when(constructorCallExecutor.execute(method, invocation)).thenReturn(statement);

        CallStatement resolvedStatement = resolver.resolve(invocation);

        assertThat(resolvedStatement).isSameAs(statement);
    }
}