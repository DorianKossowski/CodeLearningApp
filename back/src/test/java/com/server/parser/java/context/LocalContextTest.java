package com.server.parser.java.context;

import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.java.variable.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

class LocalContextTest {
    private static final String TYPE = "int";
    private static final String NAME = "name";
    private static final String NAME_2 = "name2";
    private final Map<String, FieldVar> nameToField = new HashMap<>();
    private final Map<String, Variable> nameToVariable = new HashMap<>();

    @Mock
    private Value value;
    @Mock
    private ObjectValue objectValue;
    @Mock
    private DelegatingContext context;

    private LocalContext localContext;
    private MethodVar methodVar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        methodVar = new MethodVar(TYPE, NAME, value);
        setUpLocalContext();
    }

    private void setUpLocalContext() {
        when(context.getFields()).thenReturn(nameToField);
        when(context.getImmutableVariables()).thenReturn(nameToVariable);
        when(context.getThisValue()).thenReturn(objectValue);
        localContext = new LocalContext(context);
    }

    @Test
    void shouldAddLocalVariable() {
        localContext.addVariable(methodVar);

        assertThat(localContext.getImmutableVariables()).containsExactly(entry(NAME, methodVar));
    }

    @Test
    void shouldThrowWhenAddLocalVariable() {
        nameToVariable.put(NAME, methodVar);

        assertThatThrownBy(() -> localContext.addVariable(methodVar))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt name już istnieje");
    }

    @Test
    void shouldGetLocalVariable() {
        localContext.addVariable(methodVar);

        assertThat(localContext.getVariable(NAME)).isSameAs(methodVar);
    }

    @Test
    void shouldGetVariable() {
        nameToVariable.put(NAME, methodVar);
        when(context.getVariable(NAME)).thenReturn(methodVar);

        assertThat(localContext.getVariable(NAME)).isSameAs(methodVar);
    }

    @Test
    void shouldCreateLocalContext() {
        nameToVariable.put(NAME, methodVar);
        Variable variable2 = new MethodVar(TYPE, NAME_2, value);
        localContext.addVariable(variable2);

        LocalContext newLocalContext = (LocalContext) localContext.createLocalContext();

        assertThat(newLocalContext.getImmutableVariables()).containsExactly(entry(NAME, methodVar), entry(NAME_2, variable2));
    }
}