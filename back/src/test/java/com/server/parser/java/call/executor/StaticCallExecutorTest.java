package com.server.parser.java.call.executor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        JavaParser.MethodBodyContext bodyContext = mock(JavaParser.MethodBodyContext.class, RETURNS_DEEP_STUBS);
        Method method = mockMethod(bodyContext);
        ExpressionStatement expressionStatement = mockExpressionStatement();
        doReturn(javaContext).when(spyExecutor).createStaticExecutionContext(method);
        when(javaContext.resolveStatements(javaContext, bodyContext.statementList())).thenReturn(Collections.singletonList(expressionStatement));

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

    @Test
    void shouldCallPrintMethod() {
        String text = "text";
        String method = "method";
        String name = "System.out.print";
        List<Expression> args = Collections.singletonList(mock(Expression.class));
        CallInvocation invocation = new CallInvocation(text, method, new CallReference(name), args);

        CallStatement call = executor.executePrintMethod(invocation);

        assertThat(((CallInvocation) Iterables.getOnlyElement(call.getExpressionStatements())))
                .returns(text, Statement::getText)
                .returns(method, CallInvocation::printMethodName)
                .returns(name, CallInvocation::getName)
                .returns(args, CallInvocation::getArgs);
        assertThat(call.getResult()).isSameAs(VoidExpression.INSTANCE);
    }

    @Test
    void shouldThrowWhenInvalidArgsForPrint() {
        assertThatThrownBy(() -> executor.executePrintMethod(new CallInvocation("", "",
                new CallReference("System.out.print"), Collections.emptyList())))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Metoda System.out.print musi przyjmować tylko jeden argument (wywołano z 0)");
    }
}