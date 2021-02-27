package com.server.parser.java.ast.value;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ObjectValueTest {

    static Stream<Arguments> equalsOperatorProvider() {
        Instance instance = mock(Instance.class);
        ObjectValue thisValue = new ObjectValue(instance);
        return Stream.of(
                Arguments.of(thisValue, NullValue.INSTANCE, false),
                Arguments.of(thisValue, new ObjectValue(instance), true),
                Arguments.of(thisValue, new ObjectValue(mock(Instance.class)), false)
        );
    }

    @ParameterizedTest
    @MethodSource("equalsOperatorProvider")
    void shouldEqualsOperator(ObjectValue thisValue, Value toCompareValue, boolean result) {
        assertThat(thisValue.equalsOperator(toCompareValue)).isEqualTo(result);
    }

    static Stream<Arguments> equalsOperatorThrowingProvider() {
        ObjectValue thisValue = new ObjectValue(mock(Instance.class));
        Literal literal = new Literal(new BooleanConstant());
        return Stream.of(
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression("NAME")), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void"),
                Arguments.of(thisValue, new PrimitiveValue(literal), "Nie można porównać typu obiektowego z false"),
                Arguments.of(thisValue, new ObjectWrapperValue(literal), "Nie można porównać typu obiektowego z false")
        );
    }

    @ParameterizedTest
    @MethodSource("equalsOperatorThrowingProvider")
    void shouldThrowWhenEqualsOperator(ObjectValue thisValue, Value toCompareValue, String exceptionMessageContent) {
        assertThatThrownBy(() -> thisValue.equalsOperator(toCompareValue))
                .isInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: " + exceptionMessageContent);
    }

    static Stream<Arguments> equalsMethodProvider() {
        Instance instance = mock(Instance.class);
        ObjectValue thisValue = new ObjectValue(instance);
        Literal literal = new Literal(new BooleanConstant());
        return Stream.of(
                Arguments.of(thisValue, NullValue.INSTANCE, false),
                Arguments.of(thisValue, new ObjectValue(instance), true),
                Arguments.of(thisValue, new ObjectValue(mock(Instance.class)), false),
                Arguments.of(thisValue, new PrimitiveValue(literal), false),
                Arguments.of(thisValue, new ObjectWrapperValue(literal), false)
        );
    }

    @ParameterizedTest
    @MethodSource("equalsMethodProvider")
    void shouldEqualsMethod(ObjectValue thisValue, Value toCompareValue, boolean result) {
        assertThat(thisValue.equalsMethod(toCompareValue)).isEqualTo(result);
    }

    static Stream<Arguments> equalsMethodThrowingProvider() {
        ObjectValue thisValue = new ObjectValue(mock(Instance.class));
        return Stream.of(
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression("NAME")), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void")
        );
    }

    @ParameterizedTest
    @MethodSource("equalsMethodThrowingProvider")
    void shouldThrowWhenEqualsMethod(ObjectValue thisValue, Value toCompareValue, String exceptionMessageContent) {
        assertThatThrownBy(() -> thisValue.equalsMethod(toCompareValue))
                .isInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: " + exceptionMessageContent);
    }
}