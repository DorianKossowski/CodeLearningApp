package com.server.parser.java.value;

import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class ValueTestBase {

    @ParameterizedTest
    @MethodSource("equalsOperatorProvider")
    void shouldEqualsOperator(Value thisValue, Value toCompareValue, boolean result) {
        assertThat(thisValue.equalsOperator(toCompareValue)).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("equalsOperatorThrowingProvider")
    void shouldThrowWhenEqualsOperator(Value thisValue, Value toCompareValue, String exceptionMessageContent) {
        assertThatThrownBy(() -> thisValue.equalsOperator(toCompareValue))
                .isInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: " + exceptionMessageContent);
    }

    @ParameterizedTest
    @MethodSource("equalsMethodProvider")
    void shouldEqualsMethod(Value thisValue, Value toCompareValue, boolean result) {
        assertThat(thisValue.equalsMethod(toCompareValue)).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("equalsMethodThrowingProvider")
    void shouldThrowWhenEqualsMethod(Value thisValue, Value toCompareValue, String exceptionMessageContent) {
        assertThatThrownBy(() -> thisValue.equalsMethod(toCompareValue))
                .isInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: " + exceptionMessageContent);
    }
}
