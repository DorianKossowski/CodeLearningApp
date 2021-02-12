package com.server.parser.java.call;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.ExpressionStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CallExecutorTest {
    @Mock
    private StatementListVisitor visitor;

    private CallExecutor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new CallExecutor(visitor);
    }

    @Test
    void shouldCallPrintMethod() {
        String text = "text";
        String method = "method";
        String name = "System.out.print";
        List<Expression> args = Collections.singletonList(mock(Expression.class));
        CallInvocation invocation = new CallInvocation(text, method, name, args);

        CallStatement call = executor.callPrintMethod(invocation);

        assertThat(((CallInvocation) Iterables.getOnlyElement(call.getExpressionStatements())))
                .returns(text, Statement::getText)
                .returns(method, CallInvocation::printMethodName)
                .returns(name, CallInvocation::getName)
                .returns(args, CallInvocation::getArgs);
    }

    @Test
    void shouldThrowWhenInvalidArgsForPrint() {
        assertThatThrownBy(() -> executor.callPrintMethod(new CallInvocation("", "", "System.out.print",
                Collections.emptyList())))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Metoda System.out.print musi przyjmować tylko jeden argument (wywołano z 0)");
    }

    @Test
    void shouldExecuteNoArgCall() {
        Method method = mock(Method.class);
        JavaParser.MethodBodyContext bodyContext = mock(JavaParser.MethodBodyContext.class);
        when(method.getBodyContext()).thenReturn(bodyContext);
        ExpressionStatement expressionStatement = mock(ExpressionStatement.class);
        doCallRealMethod().when(expressionStatement).getExpressionStatements();
        when(visitor.visit(eq(bodyContext), any())).thenReturn(Collections.singletonList(expressionStatement));
        CallInvocation invocation = mock(CallInvocation.class);

        CallStatement callStatement = executor.execute(method, invocation);

        assertThat(callStatement.getCallInvocation()).isSameAs(invocation);
        assertThat(callStatement.getExpressionStatements()).containsExactly(invocation, expressionStatement);
    }
}