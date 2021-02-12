package com.server.parser.java.context;

import com.server.parser.java.ast.Variable;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContextCopyFactoryTest {

    @Test
    void shouldCreateExecutionContext() {
        String key = "KEY";
        Variable variable = mock(Variable.class);
        when(variable.isStatic()).thenReturn(true);
        ClassContext classContext = mock(ClassContext.class);
        when(classContext.getFields()).thenReturn(new HashMap<String, Variable>() {{
            put(key, variable);
        }});

        JavaContext executionContext = ContextCopyFactory.createExecutionContext(new MethodContext(classContext));

        assertThat(executionContext.getStaticFields().get(key)).isSameAs(variable);
    }
}