package com.server.parser.java.context;

import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class LocalContextTest {
    private static final String NAME = "name";
    private static final String NAME_2 = "name2";
    private final Map<String, Variable> nameToField = new HashMap<>();
    private final Map<String, Variable> nameToVariable = new HashMap<>();

    @Mock
    private Value value;

    private LocalContext localContext;
    private Variable variable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        variable = new Variable("type", NAME, value);
        localContext = new LocalContext(nameToField, nameToVariable, "", false);
    }

    @Test
    void shouldAddLocalVariable() {
        localContext.addVariable(variable);

        assertThat(localContext.getNameToVariable()).containsExactly(entry(NAME, variable));
    }

    @Test
    void shouldThrowWhenAddLocalVariable() {
        nameToVariable.put(NAME, variable);

        assertThatThrownBy(() -> localContext.addVariable(variable))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt name już istnieje");
    }

    @Test
    void shouldGetLocalVariable() {
        localContext.addVariable(variable);

        assertThat(localContext.getVariable(NAME)).isSameAs(variable);
    }

    @Test
    void shouldGetVariable() {
        nameToVariable.put(NAME, variable);

        assertThat(localContext.getVariable(NAME)).isSameAs(variable);
    }

    @Test
    void shouldGetField() {
        nameToField.put(NAME, variable);

        assertThat(localContext.getVariable(NAME)).isSameAs(variable);
    }

    @Test
    void shouldThrowWhenGettingNonStaticFieldFromStatic() {
        nameToField.put(NAME, variable);
        localContext = new LocalContext(nameToField, nameToVariable, "", true);

        assertThatThrownBy(() -> localContext.getVariable(NAME))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można użyć name ze statycznego kontekstu");
    }

    @Test
    void shouldCreateLocalContext() {
        nameToVariable.put(NAME, variable);
        Variable variable2 = new Variable("type", NAME_2, value);
        localContext.addVariable(variable2);

        LocalContext newLocalContext = (LocalContext) localContext.createLocalContext();

        assertThat(newLocalContext.getNameToVariable()).containsExactly(entry(NAME, variable), entry(NAME_2, variable2));
    }
}