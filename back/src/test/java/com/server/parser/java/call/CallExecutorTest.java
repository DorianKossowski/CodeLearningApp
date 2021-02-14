package com.server.parser.java.call;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static com.server.parser.java.call.CallExecutor.MAX_EXECUTION_LEVEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CallExecutorTest {
    @Mock
    private StatementListVisitor visitor;
    @Mock
    private JavaContext context;

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
        CallInvocation invocation = new CallInvocation(text, method, new CallReference(name), args);

        CallStatement call = executor.callPrintMethod(invocation);

        assertThat(((CallInvocation) Iterables.getOnlyElement(call.getExpressionStatements())))
                .returns(text, Statement::getText)
                .returns(method, CallInvocation::printMethodName)
                .returns(name, CallInvocation::getName)
                .returns(args, CallInvocation::getArgs);
    }

    @Test
    void shouldThrowWhenInvalidArgsForPrint() {
        assertThatThrownBy(() -> executor.callPrintMethod(new CallInvocation("", "",
                new CallReference("System.out.print"), Collections.emptyList())))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Metoda System.out.print musi przyjmować tylko jeden argument (wywołano z 0)");
    }

    @Test
    void shouldExecuteNoArgCall() {
        Method method = mock(Method.class, RETURNS_DEEP_STUBS);
        JavaParser.MethodBodyContext bodyContext = mock(JavaParser.MethodBodyContext.class);
        when(method.getBodyContext()).thenReturn(bodyContext);
        when(method.getHeader().getArguments()).thenReturn(Collections.emptyList());
        ExpressionStatement expressionStatement = mock(ExpressionStatement.class);
        doCallRealMethod().when(expressionStatement).getExpressionStatements();
        when(visitor.visit(eq(bodyContext), any())).thenReturn(Collections.singletonList(expressionStatement));
        CallInvocation invocation = mock(CallInvocation.class);

        CallStatement callStatement = executor.execute(method, invocation);

        assertThat(callStatement.getCallInvocation()).isSameAs(invocation);
        assertThat(callStatement.getExpressionStatements()).containsExactly(invocation, expressionStatement);
    }

    @Test
    void shouldThrowWhenTooManyExecutions() {
        for (int i = 0; i <= MAX_EXECUTION_LEVEL; ++i) {
            executor.prepareForNextExecution();
        }

        assertThatThrownBy(() -> executor.prepareForNextExecution())
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Przekroczono ilość dopuszczalnych zagnieżdżonych wywołań równą: 10");
    }

    @Test
    void shouldAssignParameters() {
        String name = "name";
        VariableDef variableDef = mock(VariableDef.class);
        when(variableDef.getName()).thenReturn(name);
        List<VariableDef> arguments = Collections.singletonList(variableDef);
        Expression expression = mock(Expression.class);
        List<Expression> invocationParameters = Collections.singletonList(expression);

        executor.assignInvocationParameters(arguments, invocationParameters, context);

        verify(context).updateVariable(name, expression);
    }
}