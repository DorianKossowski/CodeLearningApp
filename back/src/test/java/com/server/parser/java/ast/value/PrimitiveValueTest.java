package com.server.parser.java.ast.value;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void shouldThrowWhenGetAttribute() {
        assertThatThrownBy(() -> new PrimitiveValue(mock(Literal.class)).getAttribute("NAME"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można uzyskiwać wartości NAME z prymitywa");
    }

    @Test
    void shouldThrowWhenUpdateAttribute() {
        assertThatThrownBy(() -> new PrimitiveValue(mock(Literal.class)).updateAttribute("NAME", mock(Expression.class)))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można aktualizować wartości NAME z prymitywa");
    }
}