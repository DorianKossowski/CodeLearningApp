package com.server.parser.java.context;

import com.server.parser.java.ast.FieldVar;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContextCopyFactoryTest {
    private static final String FIELD_NAME = "FIELD";

    @Test
    void shouldCreateExecutionContext() {
        FieldVar variable = mock(FieldVar.class);
        when(variable.isStatic()).thenReturn(true);
        ClassContext classContext = mock(ClassContext.class);
        when(classContext.getFields()).thenReturn(new HashMap<String, FieldVar>() {{
            put(FIELD_NAME, variable);
        }});

        JavaContext executionContext = ContextCopyFactory.createExecutionContext(new MethodContext(classContext));

        assertThat(executionContext.getStaticFields().get(FIELD_NAME)).isSameAs(variable);
    }
}