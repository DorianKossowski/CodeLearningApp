package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.task.model.MethodArgs;
import com.server.parser.java.task.model.MethodModel;
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
        TaskAst taskAst = mockTask(Arrays.asList(mockMethod(name), mockMethod(name2)));
        MethodVerifier methodVerifier = new MethodVerifier(taskAst);

        methodVerifier.verify(MethodModel.builder().withName(name).build());
    }

    private TaskAst mockTask(List<Method> methods) {
        TaskAst taskAst = mock(TaskAst.class, RETURNS_DEEP_STUBS);
        when(taskAst.getClassAst().getBody().getMethods()).thenReturn(methods);
        return taskAst;
    }

    private Method mockMethod(String name) {
        Method method = mock(Method.class, RETURNS_DEEP_STUBS);
        when(method.getHeader().getName()).thenReturn(name);
        return method;
    }

    @Test
    void shouldThrowDuringVerifyingMethodName() {
        TaskAst taskAst = mockTask(Collections.singletonList(mockMethod("NAME")));
        MethodVerifier methodVerifier = new MethodVerifier(taskAst);

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