package com.server.parser.util;

import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.ObjectRef;
import com.server.parser.java.ast.constant.*;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class VariableValidatorTest {

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
    void shouldValidateCorrectLiteralExpression(String type, Literal literal) {
        assertDoesNotThrow(() -> VariableValidator.validate(type, literal));
    }

    @Test
    void shouldThrowWhenInvalidLiteralExpression() {
        assertThatThrownBy(() -> {
            Literal l = new Literal(new CharacterConstant('L'));
            VariableValidator.validate("String", l);
        })
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie 'L' nie jest typu String (rzutowanie nie jest " +
                        "wspierane)");
    }

    @Test
    void shouldThrowWhenInvalidObjectRef() {
        assertThatThrownBy(() -> {
            Literal l = new Literal(new CharacterConstant('L'));
            VariableValidator.validate("String", new ObjectRef("x", l));
        })
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie x nie jest typu String (rzutowanie nie jest " +
                        "wspierane)");
    }
}