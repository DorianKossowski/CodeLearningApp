package com.server.parser.java.context;

import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ClassContextTest {
    private static final String NAME = "name";

    private ClassContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new ClassContext();
    }

    @Test
    void shouldAddLocalVariable() {
        Variable variable = new Variable("", NAME, mock(Value.class));
        context.addField(variable);

        assertThat(context.getFields().get(NAME)).isSameAs(variable);
    }

    @Test
    void shouldThrowWhenAddFieldAgain() {
        Variable variable = new Variable("", NAME, mock(Value.class));
        context.addField(variable);

        assertThatThrownBy(() -> context.addField(variable))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Pole name już istnieje");
    }
}