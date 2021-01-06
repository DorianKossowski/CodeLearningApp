package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.model.MethodModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ConstructorVerifierTest extends VerifierTestBase {

    @Test
    void shouldVerifyConstructor() {
        String name = "NAME";
        String name2 = "NAME2";
        Task task = mockTask(Collections.singletonList(mockMethod(name2)));
        Method constructor = mockMethod(name);
        when(task.getClassAst().getBody().getConstructors()).thenReturn(Collections.singletonList(constructor));
        MethodVerifier methodVerifier = new ConstructorVerifier(task);

        methodVerifier.verify(MethodModel.builder().withName(name).build());
    }

    @Test
    void shouldThrowWhenLackOfConstructor() {
        String name = "NAME";
        Task task = mockTask(Collections.singletonList(mockMethod(name)));
        when(task.getClassAst().getBody().getConstructors()).thenReturn(Collections.emptyList());
        MethodVerifier methodVerifier = new ConstructorVerifier(task);

        assertThatThrownBy(() -> methodVerifier.verify(MethodModel.builder().withName(name).build()))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessage("Oczekiwany konstruktor \"NAME\" nie istnieje");
    }

}