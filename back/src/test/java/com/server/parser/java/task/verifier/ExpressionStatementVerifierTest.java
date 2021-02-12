package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.ExpressionStatement;
import com.server.parser.java.ast.statement.StatementProperties;
import com.server.parser.java.task.model.StatementModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExpressionStatementVerifierTest extends VerifierTestBase {
    private static final String METHOD_NAME = "M1";
    private static final String STATEMENT = "fun(x);";

    @Test
    void shouldVerifyMethodName() {
        CallInvocation callInvocation = mock(CallInvocation.class);
        when(callInvocation.printMethodName()).thenReturn(METHOD_NAME);
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(callInvocation)));

        verifier.verify(StatementModel.builder().withMethod(METHOD_NAME).build());
    }

    @Test
    void shouldThrowDuringVerifyingMethodName() {
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods));

        assertThatThrownBy(() -> verifier.verify(StatementModel.builder().withMethod(METHOD_NAME).build()))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessage("Oczekiwana instrukcja nie istnieje");
    }

    @Test
    void shouldVerifyText() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getText()).thenReturn(STATEMENT);
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withText(STATEMENT).build());
    }

    @Test
    void shouldVerifyResolved() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getResolved()).thenReturn(STATEMENT);
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withResolved(STATEMENT).build());
    }

    @Test
    void shouldVerifyIfCond() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.IF_CONDITION)).thenReturn("cond");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withIf("cond").build());
    }

    @Test
    void shouldVerifyElseIfCond() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.IF_CONDITION)).thenReturn("cond");
        when(statement.getProperty(StatementProperties.IN_ELSE)).thenReturn("true");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withElseIf("cond").build());
    }

    @Test
    void shouldVerifyIsInElse() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.IN_ELSE)).thenReturn("true");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().isInElse(true).build());
    }

    @Test
    void shouldVerifySwitchExpr() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.SWITCH_EXPRESSION)).thenReturn("expr");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withSwitchExpr("expr").build());
    }

    @Test
    void shouldVerifySwitchLabel() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.SWITCH_LABELS)).thenReturn("label");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withSwitchLabel("label").build());
    }

    @Test
    void shouldThrowCorrectErrorMessage() {
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods));
        StatementModel model = StatementModel.builder()
                .withMethod(METHOD_NAME)
                .withLogInfo("Wywołanie metody wewnątrz M1")
                .build();

        assertThatThrownBy(() -> verifier.verify(model))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessage("Oczekiwana instrukcja \"Wywołanie metody wewnątrz M1\" nie istnieje");
    }

    @Test
    void shouldVerifyForIteration() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.FOR_ITERATION)).thenReturn("1");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withForIteration(1).build());
    }

    @Test
    void shouldVerifyWhileIteration() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.WHILE_ITERATION)).thenReturn("1");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withWhileIteration(1).build());
    }

    @Test
    void shouldVerifyDoWhileIteration() {
        ExpressionStatement statement = mock(ExpressionStatement.class);
        when(statement.getProperty(StatementProperties.DO_WHILE_ITERATION)).thenReturn("1");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME));
        ExpressionStatementVerifier verifier = new ExpressionStatementVerifier(mockTask(methods, Collections.singletonList(statement)));

        verifier.verify(StatementModel.builder().withDoWhileIteration(1).build());
    }
}