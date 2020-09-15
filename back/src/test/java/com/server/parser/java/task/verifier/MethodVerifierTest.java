package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.Exercise;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.task.MethodModel;
import com.server.parser.java.task.ast.MethodArgs;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MethodVerifierTest {

    @Test
    void shouldVerifyMethodName() {
        String name = "NAME";
        String name2 = "NAME2";
        Exercise exercise = mockExercise(Arrays.asList(mockMethod(name), mockMethod(name2)));
        MethodVerifier methodVerifier = new MethodVerifier(exercise);

        methodVerifier.verify(MethodModel.builder().withName(name).build());
    }

    private Exercise mockExercise(List<Method> methods) {
        Exercise exercise = mock(Exercise.class, RETURNS_DEEP_STUBS);
        when(exercise.getClassAst().getBody().getMethods()).thenReturn(methods);
        return exercise;
    }

    private Method mockMethod(String name) {
        Method method = mock(Method.class, RETURNS_DEEP_STUBS);
        when(method.getHeader().getName()).thenReturn(name);
        return method;
    }

    @Test
    void shouldThrowDuringVerifyingMethodName() {
        Exercise exercise = mockExercise(Collections.singletonList(mockMethod("NAME")));
        MethodVerifier methodVerifier = new MethodVerifier(exercise);

        assertThatThrownBy(() -> methodVerifier.verify(MethodModel.builder().withName("NOT_NAME").build()))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessageContaining("Oczekiwana metoda: NOT_NAME nie istnieje");
    }

    @Test
    void shouldHasSameMethodArgs() {
        List<Variable> actualArgs = Arrays.asList(new Variable("int", "name1"), new Variable("String", "name2"));
        List<MethodArgs> expectedArgs = Arrays.asList(new MethodArgs(null, "name1"), new MethodArgs("String", "name2"));

        assertThat(MethodVerifier.hasSameMethodArgs(actualArgs, expectedArgs)).isTrue();
    }
}