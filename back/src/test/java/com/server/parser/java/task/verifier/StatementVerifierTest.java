package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodCall;
import com.server.parser.java.ast.Statement;
import com.server.parser.java.task.model.StatementModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatementVerifierTest extends VerifierTestBase {
    private static final String METHOD_NAME = "M1";
    private static final String STATEMENT = "fun(x)";

    @Test
    void shouldVerifyMethodName() {
        MethodCall statement = mock(MethodCall.class);
        when(statement.printMethodName()).thenReturn(METHOD_NAME);
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME,
                Collections.singletonList(statement)));
        StatementVerifier verifier = new StatementVerifier(mockTask(methods));

        verifier.verify(StatementModel.builder().withMethod(METHOD_NAME).build());
    }

    @Test
    void shouldThrowDuringVerifyingMethodName() {
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME, Collections.emptyList()));
        StatementVerifier verifier = new StatementVerifier(mockTask(methods));

        assertThatThrownBy(() -> verifier.verify(StatementModel.builder().withMethod(METHOD_NAME).build()))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessage("Oczekiwana instrukcja nie istnieje");
    }

    @Test
    void shouldVerifyText() {
        Statement statement = mock(Statement.class);
        when(statement.getText()).thenReturn(STATEMENT);
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME, Collections.singletonList(statement)));
        StatementVerifier verifier = new StatementVerifier(mockTask(methods));

        verifier.verify(StatementModel.builder().withText(STATEMENT).build());
    }

    @Test
    void shouldVerifyResolved() {
        Statement statement = mock(Statement.class);
        when(statement.getResolved()).thenReturn(STATEMENT);
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME, Collections.singletonList(statement)));
        StatementVerifier verifier = new StatementVerifier(mockTask(methods));

        verifier.verify(StatementModel.builder().withResolved(STATEMENT).build());
    }

    @Test
    void shouldThrowCorrectErrorMessage() {
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME, Collections.emptyList()));
        StatementVerifier verifier = new StatementVerifier(mockTask(methods));
        StatementModel model = StatementModel.builder()
                .withMethod(METHOD_NAME)
                .withLogInfo("Wywołanie metody wewnątrz M1")
                .build();

        assertThatThrownBy(() -> verifier.verify(model))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessage("Oczekiwana instrukcja \"Wywołanie metody wewnątrz M1\" nie istnieje");
    }
}