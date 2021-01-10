package com.server.parser.java.context;

import com.server.parser.java.ast.MethodHeader;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClassContextTest {
    @Mock
    private MethodContext methodContext;
    @Mock
    private MethodHeader methodHeader;

    private final ClassContext context = new ClassContext();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldPutMethodWithContext() {
        context.saveCurrentMethodContext(methodContext, methodHeader);

        assertThat(context.getMethodWithContext()).containsExactly(entry(methodHeader, methodContext));
    }

    @Test
    void shouldThrowWhenMethodAlreadyExists() {
        when(methodHeader.toString()).thenReturn("NAZWA");
        context.saveCurrentMethodContext(methodContext, methodHeader);

        assertThatThrownBy(() -> context.saveCurrentMethodContext(methodContext, methodHeader))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Metoda NAZWA już istnieje");
    }
}