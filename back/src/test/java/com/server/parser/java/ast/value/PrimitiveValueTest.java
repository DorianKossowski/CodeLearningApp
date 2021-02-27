package com.server.parser.java.ast.value;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

class PrimitiveValueTest extends ValueTestBase {

    static Stream<Arguments> equalsOperatorProvider() {
        Literal literal = new Literal(new BooleanConstant());
        Literal literalCopy = new Literal(new BooleanConstant());
        PrimitiveValue thisValue = new PrimitiveValue(literal);
        return Stream.of(
                Arguments.of(thisValue, new PrimitiveValue(literal), true),
                Arguments.of(thisValue, new PrimitiveValue(literalCopy), true),
                Arguments.of(thisValue, new ObjectWrapperValue(literal), true),
                Arguments.of(thisValue, new ObjectWrapperValue(literalCopy), true)
        );
    }

    static Stream<Arguments> equalsOperatorThrowingProvider() {
        Literal literal = new Literal(new BooleanConstant());
        PrimitiveValue thisValue = new PrimitiveValue(literal);
        return Stream.of(
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression("NAME")), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void"),
                Arguments.of(thisValue, NullValue.INSTANCE, "Nie można porównać z null"),
                Arguments.of(thisValue, new ObjectValue(new Instance("NAME", Collections.emptyMap())),
                        "Nie można porównać z instancja NAME")
        );
    }


    @Override
    void shouldEqualsMethod(Value thisValue, Value toCompareValue, boolean result) {
        // NOT SUPPORTED
    }

    static Stream<Arguments> equalsMethodThrowingProvider() {
        PrimitiveValue thisValue = new PrimitiveValue(mock(Literal.class));
        return Stream.of(
                Arguments.of(thisValue, mock(Value.class), "Nie można wywołać metody equals na prymitywie")
        );
    }
}