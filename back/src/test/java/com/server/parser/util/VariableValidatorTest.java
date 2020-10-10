package com.server.parser.util;

import com.server.parser.java.ast.Literal;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class VariableValidatorTest {

    @Test
    void shouldValidateCorrectLiteralExpression() {
        assertDoesNotThrow(() -> VariableValidator.validate("String", new Literal("L")));
    }

    @Test
    void shouldThrowWhenInvalidLiteralExpression() {
        assertThatThrownBy(() -> VariableValidator.validate("String", new Literal('L')))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie L nie jest typu String");
    }
}