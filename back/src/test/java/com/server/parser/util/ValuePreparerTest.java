package com.server.parser.util;

import com.server.parser.java.ast.constant.*;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.expression.ObjectRef;
import com.server.parser.java.ast.value.NullValue;
import com.server.parser.java.ast.value.PrimitiveValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ValuePreparerTest {

    static Stream<Arguments> typeWithLiteralProvider() {
        return Stream.of(
                Arguments.of("String", new Literal(new StringConstant("L"))),
                Arguments.of("char", new Literal(new CharacterConstant('c'))),
                Arguments.of("Character", new Literal(new CharacterConstant('c'))),
                Arguments.of("int", new Literal(new IntConstant(1))),
                Arguments.of("Integer", new Literal(new IntConstant(1))),
                Arguments.of("Byte", new Literal(new IntConstant(1))),
                Arguments.of("byte", new Literal(new IntConstant(1))),
                Arguments.of("short", new Literal(new IntConstant(1))),
                Arguments.of("Short", new Literal(new IntConstant(1))),
                Arguments.of("double", new Literal(new DoubleConstant(1.0))),
                Arguments.of("Double", new Literal(new DoubleConstant(1.0))),
                Arguments.of("float", new Literal(new DoubleConstant(1.0))),
                Arguments.of("Float", new Literal(new DoubleConstant(1.0))),
                Arguments.of("boolean", new Literal(new BooleanConstant(false))),
                Arguments.of("Boolean", new Literal(new BooleanConstant(true)))
        );
    }

    @ParameterizedTest
    @MethodSource("typeWithLiteralProvider")
    void shouldPrepareCorrectLiteralExpression(String type, Literal literal) {
        assertDoesNotThrow(() -> ValuePreparer.prepare(type, literal));
    }

    @Test
    void shouldThrowWhenInvalidLiteralExpression() {
        assertThatThrownBy(() -> {
            Literal l = new Literal(new CharacterConstant('L'));
            ValuePreparer.prepare("String", l);
        })
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie 'L' nie jest typu String");
    }

    @Test
    void shouldThrowWhenInvalidObjectRef() {
        assertThatThrownBy(() -> {
            Literal l = new Literal(new CharacterConstant('L'));
            ValuePreparer.prepare("String", new ObjectRef("x", new PrimitiveValue(l)));
        })
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie x nie jest typu String");
    }

    @Test
    void shouldCastIntToDouble() {
        Literal expression = new Literal(new IntConstant(1));

        PrimitiveValue value = (PrimitiveValue) ValuePreparer.prepare("double", expression);

        Constant<?> resolved = value.getConstant();
        assertThat(resolved).isExactlyInstanceOf(DoubleConstant.class);
        assertThat(resolved.c).isEqualTo(1.0);
    }

    @Test
    void shouldPrepareComplexObjectRef() {
        Literal l = new Literal(new CharacterConstant('L'));
        ObjectRef obj1 = new ObjectRef("obj1", new PrimitiveValue(l));
        ObjectRef obj2 = new ObjectRef("obj2", obj1.getValue());

        PrimitiveValue value = (PrimitiveValue) ValuePreparer.prepare("char", obj2);

        Constant<?> resolved = value.getConstant();
        assertThat(resolved).isExactlyInstanceOf(CharacterConstant.class);
        assertThat(resolved.c).isEqualTo('L');
    }

    @Test
    void shouldPreparePrimitive() {
        Literal l = new Literal(new CharacterConstant('L'));

        PrimitiveValue value = ValuePreparer.preparePrimitive("Character", l);

        assertThat(value.getConstant().c).isEqualTo('L');
    }

    @Test
    void shouldPrepareWhenNull() {
        Value value = ValuePreparer.prepare("String", NullExpression.INSTANCE);

        assertThat(value).isExactlyInstanceOf(NullValue.class);
    }
}