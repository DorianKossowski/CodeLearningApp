package com.server.parser.java.ast.value;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

class NullValueTest extends ValueTestBase {

    static Stream<Arguments> equalsOperatorProvider() {
        NullValue thisValue = NullValue.INSTANCE;
        return Stream.of(
                Arguments.of(thisValue, NullValue.INSTANCE, true),
                Arguments.of(thisValue, new ObjectValue(mock(Instance.class)), false)
        );
    }

    static Stream<Arguments> equalsOperatorThrowingProvider() {
        NullValue thisValue = NullValue.INSTANCE;
        Literal literal = new Literal(new BooleanConstant());
        return Stream.of(
                Arguments.of(thisValue, new PrimitiveValue(literal), "Nie można porównać z false"),
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression("NAME")), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void")
        );
    }

    @Override
    void shouldEqualsMethod(Value thisValue, Value toCompareValue, boolean result) {
        // NOT SUPPORTED
    }

    static Stream<Arguments> equalsMethodThrowingProvider() {
        NullValue thisValue = NullValue.INSTANCE;
        return Stream.of(
                Arguments.of(thisValue, mock(Value.class), "NullPointerException")
        );
    }
}