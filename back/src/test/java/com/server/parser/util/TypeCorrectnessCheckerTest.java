package com.server.parser.util;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class TypeCorrectnessCheckerTest {

    @Test
    void shouldThrowWhenArrayType() {
        assertThatThrownBy(() -> TypeCorrectnessChecker.isCorrect("int[]", mock(Expression.class)))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Operacje na tablicach (int[]) nie są wspierane");
    }

    @Test
    void shouldBeCorrectForGenericType() {
        assertThat(TypeCorrectnessChecker.isCorrect("MyClass", mock(Instance.class))).isTrue();
    }
}