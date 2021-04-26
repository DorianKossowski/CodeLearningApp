package com.server.parser.java.constant;

import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void shouldAndWhenBoolean() {
        BooleanConstant b1 = new BooleanConstant(true);
        BooleanConstant b2 = new BooleanConstant(true);

        assertThat(b1.and(b2)).isTrue();
    }

    @Test
    void shouldThrowWhenBooleanAndString() {
        BooleanConstant b = new BooleanConstant(true);
        StringConstant s = new StringConstant("true");

        assertThatThrownBy(() -> b.and(s))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można użyć operatora && dla typu String");
    }

    @Test
    void shouldThrowWhenIntAndBoolean() {
        IntConstant i = new IntConstant(1);
        BooleanConstant b = new BooleanConstant(true);

        assertThatThrownBy(() -> i.and(b))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można użyć operatora && dla typu Integer");
    }


    @Test
    void shouldOrWhenBoolean() {
        BooleanConstant b1 = new BooleanConstant(true);
        BooleanConstant b2 = new BooleanConstant(false);

        assertThat(b1.or(b2)).isTrue();
    }
}