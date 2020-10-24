package com.server.parser.java.context;

import com.server.parser.java.ast.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MethodContextTest {

    @Test
    void shouldThrowWhenKeyAlreadyExists() {
        MethodContext methodContext = new MethodContext("name");
        Variable var = mock(Variable.class);
        when(var.getName()).thenReturn("var");
        methodContext.addVar(var);

        assertThatThrownBy(() -> methodContext.addVar(var))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var już istnieje");
    }

    @Test
    void shouldThrowWhenKeyNotExists() {
        MethodContext methodContext = new MethodContext("name");

        assertThatThrownBy(() -> methodContext.getVariable("var"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var nie istnieje");
    }
}