package com.server.parser.java.context;

import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MethodContextTest {

    @Test
    void shouldThrowWhenKeyNotExists() {
        MethodContext methodContext = new MethodContext("name");

        assertThatThrownBy(() -> methodContext.getValue("var"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var nie istnieje");
    }
}