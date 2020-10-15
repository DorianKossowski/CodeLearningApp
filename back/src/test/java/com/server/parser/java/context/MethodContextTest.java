package com.server.parser.java.context;

import com.server.parser.java.ast.Expression;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class MethodContextTest {

    @Test
    void shouldThrowWhenKeyAlreadyExists() {
        MethodContext methodContext = new MethodContext("name");
        methodContext.addVar("var", mock(Expression.class));

        assertThatThrownBy(() -> methodContext.addVar("var", mock(Expression.class)))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var już istnieje");
    }

    @Test
    void shouldThrowWhenKeyNotExists() {
        MethodContext methodContext = new MethodContext("name");

        assertThatThrownBy(() -> methodContext.getValue("var"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var nie istnieje");
    }
}