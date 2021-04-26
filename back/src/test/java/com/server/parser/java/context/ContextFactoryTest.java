package com.server.parser.java.context;

import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.call.CallResolver;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.variable.FieldVar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ContextFactoryTest {
    private static final String STATIC_FIELD_NAME = "STATIC_FIELD";
    private static final String FIELD_NAME = "FIELD";

    @Mock
    private FieldVar staticFieldVar;
    @Mock
    private FieldVar fieldVar;
    @Mock
    private FieldVar instanceFieldVar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(staticFieldVar.isStatic()).thenReturn(true);
        when(fieldVar.isStatic()).thenReturn(false);
    }

    @Test
    void shouldCreateStaticExecutionContext() {
        ClassContext classContext = mock(ClassContext.class);
        when(classContext.getFields()).thenReturn(new HashMap<String, FieldVar>() {{
            put(STATIC_FIELD_NAME, staticFieldVar);
        }});

        JavaContext executionContext = ContextFactory.createStaticExecutionContext(new MethodContext(classContext));

        assertThat(executionContext.getStaticFields().get(STATIC_FIELD_NAME)).isSameAs(staticFieldVar);
    }

    @Test
    void shouldHaveSameCallResolver() {
        ClassContext classContext = new ClassContext();
        classContext.setParameters(ContextParameters.createClassContextParameters(""));
        CallResolver callResolver = classContext.getParameters().getCallResolver();

        JavaContext executionContext = ContextFactory.createStaticExecutionContext(classContext);

        assertThat(executionContext.getParameters().getCallResolver()).isSameAs(callResolver);
    }

    @Test
    void shouldCreateExecutionContext() {
        ClassContext classContext = mock(ClassContext.class);
        when(classContext.getFields()).thenReturn(new HashMap<String, FieldVar>() {{
            put(STATIC_FIELD_NAME, staticFieldVar);
            put(FIELD_NAME, fieldVar);
        }});
        Instance instance = mock(Instance.class, RETURNS_DEEP_STUBS);
        when(instance.getFields()).thenReturn(new HashMap<String, FieldVar>() {{
            put(STATIC_FIELD_NAME, staticFieldVar);
            put(FIELD_NAME, instanceFieldVar);
        }});

        MethodContext executionContext = ((MethodContext) ContextFactory.createExecutionContext(new ObjectValue(instance),
                new MethodContext(classContext)));

        assertThat(executionContext.getFields().get(STATIC_FIELD_NAME)).isSameAs(staticFieldVar);
        assertThat(executionContext.getFields().get(FIELD_NAME)).isSameAs(instanceFieldVar);
    }
}