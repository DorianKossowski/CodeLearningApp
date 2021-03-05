package com.server.parser.java.ast.value;

import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.FieldVarInitExpressionFunction;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.java.constant.BooleanConstant;
import com.server.parser.java.constant.StringConstant;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ObjectValueTest extends ValueTestBase {
    private static final String NAME = "NAME";

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
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression(NAME)), "Niezainicjalizowana zmienna NAME"),
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
                Arguments.of(thisValue, new UninitializedValue(new UninitializedExpression(NAME)), "Niezainicjalizowana zmienna NAME"),
                Arguments.of(thisValue, VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void")
        );
    }

    @Test
    void shouldThrowWhenGetUnknownAttribute() {
        assertThatThrownBy(() -> new ObjectValue(new Instance("", Collections.emptyMap())).getAttribute(NAME))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można znaleźć pola NAME");
    }


    @Test
    void shouldUpdateAttribute() {
        FieldVar fieldVar = new FieldVar("String", NAME, mock(FieldVarInitExpressionFunction.class), mock(Value.class));
        ObjectValue value = new ObjectValue(new Instance("", Collections.singletonMap(NAME, fieldVar)));
        Literal newExpression = new Literal(new StringConstant(""));

        value.updateAttribute(NAME, newExpression);

        assertThat(value.getAttribute(NAME).getExpression()).isSameAs(newExpression);
    }

    @Test
    void shouldThrowWhenUpdateUnknownAttribute() {
        ObjectValue value = new ObjectValue(new Instance("", Collections.emptyMap()));
        assertThatThrownBy(() -> value.updateAttribute(NAME, mock(Expression.class)))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można znaleźć pola NAME");
    }
}