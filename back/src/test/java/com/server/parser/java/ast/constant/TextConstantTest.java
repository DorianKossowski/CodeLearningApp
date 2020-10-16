package com.server.parser.java.ast.constant;

import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextConstantTest {

    @Test
    void shouldThrowWhenStringWithInt() {
        TextConstant<String> textConstant = new TextConstant<>("str");

        assertThatThrownBy(() -> textConstant.compute(new IntConstant(1), "+"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Operacje matematyczne nie są wspierane dla typu String");
    }

    @Test
    void shouldThrowWhenCharacterWithDouble() {
        TextConstant<Character> textConstant = new TextConstant<>('c');

        assertThatThrownBy(() -> textConstant.compute(new DoubleConstant(1.0), "+"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Operacje matematyczne nie są wspierane dla typu Character");
    }
}