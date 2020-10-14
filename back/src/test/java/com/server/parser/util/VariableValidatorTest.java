package com.server.parser.util;

import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.ObjectRef;
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
                Arguments.of("String", new Literal("L")),
                Arguments.of("char", new Literal('c')),
                Arguments.of("Character", new Literal('c')),
                Arguments.of("int", new Literal(1)),
                Arguments.of("Integer", new Literal(1)),
                Arguments.of("Byte", new Literal(1)),
                Arguments.of("byte", new Literal(1)),
                Arguments.of("short", new Literal(1)),
                Arguments.of("Short", new Literal(1)),
                Arguments.of("double", new Literal(1.0)),
                Arguments.of("Double", new Literal(1.0)),
                Arguments.of("float", new Literal(1.0)),
                Arguments.of("Float", new Literal(1.0)),
                Arguments.of("boolean", new Literal(false)),
                Arguments.of("Boolean", new Literal(true))
        );
    }

    @ParameterizedTest
    @MethodSource("typeWithLiteralProvider")
    void shouldValidateCorrectLiteralExpression(String type, Literal literal) {
        assertDoesNotThrow(() -> VariableValidator.validate(type, literal));
    }

    @Test
    void shouldThrowWhenInvalidLiteralExpression() {
        assertThatThrownBy(() -> VariableValidator.validate("String", new Literal('L')))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie L nie jest typu String");
    }

    @Test
    void shouldThrowWhenInvalidObjectRef() {
        assertThatThrownBy(() -> VariableValidator.validate("String", new ObjectRef("x", new Literal('L'))))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie x nie jest typu String");
    }
}