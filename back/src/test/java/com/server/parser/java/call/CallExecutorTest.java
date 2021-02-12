package com.server.parser.java.call;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class CallExecutorTest {

    private final CallExecutor executor = new CallExecutor();

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
}