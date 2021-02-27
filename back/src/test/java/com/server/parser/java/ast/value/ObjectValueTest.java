package com.server.parser.java.ast.value;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

class ObjectValueTest extends ValueTestBase {

    static Stream<Arguments> equalsOperatorProvider() {
        Instance instance = mock(Instance.class);
        ObjectValue thisValue = new ObjectValue(instance);
        return Stream.of(
                Arguments.of(thisValue, NullValue.INSTANCE, false),
                Arguments.of(thisValue, new ObjectValue(instance), true),
                Arguments.of(thisValue, new ObjectValue(mock(Instance.class)), false)
        );
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

    static Stream<Arguments> equalsMethodThrowingProvider() {
        ObjectValue thisValue = new ObjectValue(mock(Instance.class));
        return Stream.of(
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression("NAME")), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void")
        );
    }
}