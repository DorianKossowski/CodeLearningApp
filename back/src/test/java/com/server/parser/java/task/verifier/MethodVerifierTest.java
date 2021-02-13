package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.task.model.MethodArgs;
import com.server.parser.java.task.model.MethodModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class MethodVerifierTest extends VerifierTestBase {

    @Test
    void shouldVerifyMethodName() {
        String name = "NAME";
        String name2 = "NAME2";
        Task task = mockTask(Arrays.asList(mockMethod(name), mockMethod(name2)));
        MethodVerifier methodVerifier = new MethodVerifier(task);

        methodVerifier.verify(MethodModel.builder().withName(name).build());
    }

    @Test
    void shouldThrowDuringVerifyingMethodName() {
        Task task = mockTask(Collections.singletonList(mockMethod("NAME")));
        MethodVerifier methodVerifier = new MethodVerifier(task);

        assertThatThrownBy(() -> methodVerifier.verify(MethodModel.builder().withName("NOT_NAME").build()))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessageContaining("Oczekiwana metoda \"NOT_NAME\" nie istnieje");
    }

    @Test
    void shouldHasSameMethodArgs() {
        List<VariableDef> actualArgs = Arrays.asList(new VariableDef("", "Integer", "name1", NullExpression.INSTANCE,
                false), new VariableDef("", "String", "name2", NullExpression.INSTANCE, false));
        List<MethodArgs> expectedArgs = Arrays.asList(new MethodArgs(null, "name1"), new MethodArgs("String", "name2"));

        assertThat(MethodVerifier.hasSameMethodArgs(actualArgs, expectedArgs)).isTrue();
    }

    @Test
    void shouldHasSameModifiers() {
        Method method = mockMethod("NAME");
        when(method.getHeader().getModifiers()).thenReturn(Collections.singletonList("public"));
        Task task = mockTask(Collections.singletonList(method));
        MethodVerifier methodVerifier = new MethodVerifier(task);

        methodVerifier.verify(MethodModel.builder().withModifiers(Collections.singletonList("public")).build());
    }

    @Test
    void shouldHasSameResult() {
        Method method = mockMethod("NAME");
        when(method.getHeader().getResult()).thenReturn("int");
        Task task = mockTask(Collections.singletonList(method));
        MethodVerifier methodVerifier = new MethodVerifier(task);

        methodVerifier.verify(MethodModel.builder().withResult("int").build());
    }

    @Test
    void shouldHasSameEmptyArgs() {
        Method method = mockMethod("NAME");
        when(method.getHeader().getArguments()).thenReturn(Collections.emptyList());
        Task task = mockTask(Collections.singletonList(method));
        MethodVerifier methodVerifier = new MethodVerifier(task);

        methodVerifier.verify(MethodModel.builder().withArgs(Collections.emptyList()).build());
    }
}