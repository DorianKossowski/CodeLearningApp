package com.server.parser.java.ast.value;

import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.java.constant.BooleanConstant;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ObjectWrapperValueTest extends ValueTestBase {

    static Stream<Arguments> equalsOperatorProvider() {
        Literal literal = new Literal(new BooleanConstant());
        Literal literalCopy = new Literal(new BooleanConstant());
        ObjectWrapperValue thisValue = new ObjectWrapperValue(literal);
        return Stream.of(
                Arguments.of(thisValue, new PrimitiveValue(literal), true),
                Arguments.of(thisValue, new PrimitiveValue(literalCopy), true),
                Arguments.of(thisValue, new ObjectWrapperValue(literal), true),
                Arguments.of(thisValue, new ObjectWrapperValue(literalCopy), false),
                Arguments.of(thisValue, NullValue.INSTANCE, false)
        );
    }

    static Stream<Arguments> equalsOperatorThrowingProvider() {
        Literal literal = new Literal(new BooleanConstant());
        ObjectWrapperValue thisValue = new ObjectWrapperValue(literal);
        return Stream.of(
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression("NAME")), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void"),
                Arguments.of(thisValue, new ObjectValue(new Instance("NAME", Collections.emptyMap())),
                        "Nie można porównać z instancja NAME")
        );
    }

    static Stream<Arguments> equalsMethodProvider() {
        Literal literal = new Literal(new BooleanConstant());
        Literal literalCopy = new Literal(new BooleanConstant());
        ObjectWrapperValue thisValue = new ObjectWrapperValue(literal);
        return Stream.of(
                Arguments.of(thisValue, NullValue.INSTANCE, false),
                Arguments.of(thisValue, new PrimitiveValue(literal), true),
                Arguments.of(thisValue, new PrimitiveValue(literalCopy), true),
                Arguments.of(thisValue, new ObjectWrapperValue(literal), true),
                Arguments.of(thisValue, new ObjectWrapperValue(literalCopy), true),
                Arguments.of(thisValue, new ObjectValue(new Instance("", Collections.emptyMap())), false)
        );
    }

    static Stream<Arguments> equalsMethodThrowingProvider() {
        ObjectWrapperValue thisValue = new ObjectWrapperValue(mock(Literal.class));
        return Stream.of(
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression("NAME")), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void")
        );
    }

    @Test
    void shouldThrowWhenGetAttribute() {
        assertThatThrownBy(() -> new ObjectValue(mock(Literal.class)).getAttribute("NAME"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można znaleźć pola NAME");
    }
}