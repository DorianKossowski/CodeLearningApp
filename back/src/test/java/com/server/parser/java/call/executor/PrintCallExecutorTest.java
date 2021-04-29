package com.server.parser.java.call.executor;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class PrintCallExecutorTest {
    private PrintCallExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new PrintCallExecutor();
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