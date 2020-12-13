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
        MethodContext methodContext = new MethodContext(new ClassContext());
        Variable var = mock(Variable.class);
        when(var.getName()).thenReturn("var");
        methodContext.addVariable(var);

        assertThatThrownBy(() -> methodContext.addVariable(var))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var już istnieje");
    }

    @Test
    void shouldThrowWhenKeyNotExists() {
        MethodContext methodContext = new MethodContext(new ClassContext());

        assertThatThrownBy(() -> methodContext.getVariable("var"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var nie istnieje");
    }
}