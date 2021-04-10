package com.server.parser.util;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.value.ObjectValue;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

class TypeCorrectnessCheckerTest {

    @Test
    void shouldThrowWhenArrayType() {
        assertThatThrownBy(() -> TypeCorrectnessChecker.isCorrect("int[]", mock(Expression.class)))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Operacje na tablicach (int[]) nie są wspierane");
    }

    static Stream<Arguments> shouldBeCorrectForGenericType() {
        Instance instance = mock(Instance.class);
        doCallRealMethod().when(instance).getValue();
        return Stream.of(
                Arguments.of(instance),
                Arguments.of(new ObjectRefExpression("", new ObjectValue(instance)))
        );
    }

    @ParameterizedTest
    @MethodSource
    void shouldBeCorrectForGenericType(Expression expression) {
        assertThat(TypeCorrectnessChecker.isCorrect("MyClass", expression)).isTrue();
    }
}