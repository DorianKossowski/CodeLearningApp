package com.server.parser.java.context;

import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.call.CallResolver;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContextFactoryTest {
    private static final String FIELD_NAME = "FIELD";

    @Test
    void shouldCreateExecutionContext() {
        FieldVar variable = mock(FieldVar.class);
        when(variable.isStatic()).thenReturn(true);
        ClassContext classContext = mock(ClassContext.class);
        when(classContext.getFields()).thenReturn(new HashMap<String, FieldVar>() {{
            put(FIELD_NAME, variable);
        }});

        JavaContext executionContext = ContextFactory.createStaticExecutionContext(new MethodContext(classContext));

        assertThat(executionContext.getStaticFields().get(FIELD_NAME)).isSameAs(variable);
    }

    @Test
    void shouldHaveSameCallResolver() {
        ClassContext classContext = new ClassContext();
        CallResolver callResolver = classContext.getCallResolver();

        JavaContext executionContext = ContextFactory.createStaticExecutionContext(classContext);

        assertThat(executionContext.getCallResolver()).isSameAs(callResolver);
    }
}