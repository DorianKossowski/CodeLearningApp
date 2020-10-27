package com.server.parser.java.ast.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConstantTest {

    @Test
    void shouldComputeFromCharacter() {
        CharacterConstant characterConstant = new CharacterConstant('a');
        IntConstant intConstant = new IntConstant(97);

        Constant<?> result = characterConstant.compute(intConstant, "-");

        assertThat(result).isExactlyInstanceOf(IntConstant.class);
        assertThat(result.c).isEqualTo(0);
    }

    @Test
    void shouldComputeWithCharacter() {
        DoubleConstant intConstant = new DoubleConstant(97.0);
        CharacterConstant characterConstant = new CharacterConstant('a');

        Constant<?> result = intConstant.compute(characterConstant, "-");

        assertThat(result).isExactlyInstanceOf(DoubleConstant.class);
        assertThat(result.c).isEqualTo(0.0);
    }
}