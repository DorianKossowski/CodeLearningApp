package com.server.parser.java.context;

import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.call.CallExecutor;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ClassContextTest {
    private static final String NAME = "name";

    @Mock
    private MethodContext methodContext;
    @Mock
    private MethodHeader methodHeader;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CallExecutor callExecutor;

    private ClassContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new ClassContext(callExecutor);
    }

    @Test
    void shouldPutMethodWithContext() {
        context.saveCurrentMethodContext(methodContext, methodHeader);

        verify(callExecutor.getCallableKeeper()).keepCallable(methodContext, methodHeader);
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