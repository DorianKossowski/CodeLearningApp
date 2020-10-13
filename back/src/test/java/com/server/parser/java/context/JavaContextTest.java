package com.server.parser.java.context;

import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JavaContextTest {
    private static final String METHOD_NAME = "METHOD";

    private final JavaContext context = new JavaContext();

    @Test
    void shouldPutMethodWithContext() {
        MethodContext methodContext = context.createCurrentMethodContext(METHOD_NAME);

        assertThat(context.getCurrentMethodContext()).isSameAs(methodContext);
    }

    @Test
    void shouldThrowWhenMethodAlreadyExists() {
        context.createCurrentMethodContext(METHOD_NAME);

        assertThatThrownBy(() -> context.createCurrentMethodContext(METHOD_NAME))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Metoda METHOD już istnieje");
    }
}