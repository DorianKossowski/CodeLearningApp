package com.server.parser.java.context;

import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.FieldVarInitExpressionFunction;
import com.server.parser.java.ast.MethodVar;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.call.CallResolver;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class LocalContextTest {
    private static final String TYPE = "int";
    private static final String NAME = "name";
    private static final String NAME_2 = "name2";
    private final Map<String, FieldVar> nameToField = new HashMap<>();
    private final Map<String, Variable> nameToVariable = new HashMap<>();

    @Mock
    private CallResolver callResolver;
    @Mock
    private Value value;
    @Mock
    private Expression expression;
    @Mock
    private ObjectValue objectValue;

    private LocalContext localContext;
    private MethodVar methodVar;
    private FieldVar fieldVar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        methodVar = new MethodVar(TYPE, NAME, value);
        fieldVar = new FieldVar(TYPE, NAME, new FieldVarInitExpressionFunction("", $ -> expression), value);
        localContext = new LocalContext(callResolver, nameToField, nameToVariable, "", "", "", false, objectValue);
    }

    @Test
    void shouldAddLocalVariable() {
        localContext.addVariable(methodVar);

        assertThat(localContext.getNameToVariable()).containsExactly(entry(NAME, methodVar));
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

        assertThat(localContext.getVariable(NAME)).isSameAs(methodVar);
    }

    @Test
    void shouldGetField() {
        nameToField.put(NAME, fieldVar);

        assertThat(localContext.getVariable(NAME)).isSameAs(fieldVar);
    }

    @Test
    void shouldThrowWhenGettingNonStaticFieldFromStatic() {
        nameToField.put(NAME, fieldVar);
        localContext = new LocalContext(callResolver, nameToField, nameToVariable, "", "", "", true, objectValue);

        assertThatThrownBy(() -> localContext.getVariable(NAME))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można użyć name ze statycznego kontekstu");
    }

    @Test
    void shouldCreateLocalContext() {
        nameToVariable.put(NAME, methodVar);
        Variable variable2 = new MethodVar(TYPE, NAME_2, value);
        localContext.addVariable(variable2);

        LocalContext newLocalContext = (LocalContext) localContext.createLocalContext();

        assertThat(newLocalContext.getNameToVariable()).containsExactly(entry(NAME, methodVar), entry(NAME_2, variable2));
    }
}