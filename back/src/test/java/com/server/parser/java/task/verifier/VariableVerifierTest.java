package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.task.model.StatementModel;
import com.server.parser.java.task.model.VariableModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VariableVerifierTest extends VerifierTestBase {
    private static final String METHOD_NAME = "M1";
    private static final String STATEMENT = "int x";

    @Test
    void shouldVerifyText() {
        VariableDef variableDef = mock(VariableDef.class);
        when(variableDef.getType()).thenReturn("int");
        when(variableDef.getName()).thenReturn("x");
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME, Collections.singletonList(variableDef)));
        VariableVerifier verifier = new VariableVerifier(mockTask(methods));

        verifier.verify(VariableModel.builder().withText(STATEMENT).build());
    }

    @Test
    void shouldThrowCorrectErrorMessage() {
        List<Method> methods = Collections.singletonList(mockMethod(METHOD_NAME, Collections.emptyList()));
        StatementVerifier verifier = new StatementVerifier(mockTask(methods));
        StatementModel model = StatementModel.builder()
                .withMethod(METHOD_NAME)
                .withLogInfo("Zmienna x typu int")
                .build();

        assertThatThrownBy(() -> verifier.verify(model))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessage("Oczekiwana instrukcja \"Zmienna x typu int\" nie istnieje");
    }
}