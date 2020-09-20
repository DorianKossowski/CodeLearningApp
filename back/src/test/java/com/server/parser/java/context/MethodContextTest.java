package com.server.parser.java.context;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MethodContextTest {

    @Test
    void shouldThrowWhenKeyNotExists() {
        MethodContext methodContext = new MethodContext("name");

        assertThatThrownBy(() -> methodContext.getValue("var"))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Obiekt var nie istnieje");
    }
}