package com.server.parser.java.call.executor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Statements;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import com.server.parser.java.context.JavaContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StaticCallExecutorTest {
    @Mock
    private CallInvocation invocation;
    @Mock
    private JavaContext javaContext;

    private StaticCallExecutor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new StaticCallExecutor();
    }

    @Test
    void shouldExecuteNoArgStaticCall() {
        StaticCallExecutor spyExecutor = spy(executor);
        JavaParser.MethodBodyContext bodyContext = mock(JavaParser.MethodBodyContext.class);
        Method method = mockMethod(bodyContext);
        ExpressionStatement expressionStatement = mockExpressionStatement();
        doReturn(javaContext).when(spyExecutor).createStaticExecutionContext(method);
        when(javaContext.resolveStatements(bodyContext)).thenReturn(new Statements(Collections.singletonList(expressionStatement)));

        CallStatement callStatement = spyExecutor.execute(method, invocation);

        assertThat(callStatement.getCallInvocation()).isSameAs(invocation);
        assertThat(callStatement.getExpressionStatements()).containsExactly(invocation, expressionStatement);
        assertThat(callStatement.getResult()).isSameAs(VoidExpression.INSTANCE);
    }

    private ExpressionStatement mockExpressionStatement() {
        ExpressionStatement expressionStatement = mock(ExpressionStatement.class);
        doCallRealMethod().when(expressionStatement).getExpressionStatements();
        return expressionStatement;
    }

    private Method mockMethod(JavaParser.MethodBodyContext bodyContext) {
        Method method = mock(Method.class, RETURNS_DEEP_STUBS);
        when(method.getBodyContext()).thenReturn(bodyContext);
        when(method.getHeader().getArguments()).thenReturn(Collections.emptyList());
        when(method.getHeader().getResult()).thenReturn("void");
        return method;
    }
}