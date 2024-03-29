package com.server.parser.java.call.executor;

import com.google.common.collect.ImmutableMap;
import com.rits.cloning.Cloner;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.variable.FieldVar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.*;

class ConstructorCallExecutorTest {
    private static final String CLASS_NAME = "CLASS";
    @Mock
    private Cloner cloner;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Method method;
    @Mock
    private JavaContext context;

    private ConstructorCallExecutor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new ConstructorCallExecutor(cloner);
        when(method.getClassName()).thenReturn(CLASS_NAME);
    }

    @Test
    void shouldExecute() {
        // given
        ConstructorCallExecutor spyExecutor = spy(executor);
        Instance instance = mock(Instance.class);
        FieldVar fieldVar = mock(FieldVar.class);
        when(instance.getFields()).thenReturn(Collections.singletonMap("", fieldVar));
        doReturn(instance).when(spyExecutor).prepareNewInstance(method);

        ExpressionStatement expressionStatement = mockExpressionStatement();
        List<Statement> statements = Collections.singletonList(expressionStatement);

        CallInvocation invocation = mock(CallInvocation.class);
        doReturn(statements).when(spyExecutor).executeInContext(eq(method), eq(invocation), any());

        // when
        CallStatement callStatement = spyExecutor.execute(method, invocation);

        // then
        assertThat(callStatement.getCallInvocation()).isSameAs(invocation);
        assertThat(callStatement.getResult()).isSameAs(instance);
        assertThat(callStatement.getExpressionStatements()).containsExactly(invocation, expressionStatement);
        verify(fieldVar).initialize(isA(MethodContext.class));
    }

    private ExpressionStatement mockExpressionStatement() {
        ExpressionStatement expressionStatement = mock(ExpressionStatement.class);
        doCallRealMethod().when(expressionStatement).getExpressionStatements();
        return expressionStatement;
    }

    @Test
    void shouldPrepareNewInstance() {
        // given
        ConstructorCallExecutor spyExecutor = spy(executor);
        Map<String, FieldVar> fields = mock(Map.class);
        Map<String, FieldVar> instanceFields = Collections.singletonMap("", mock(FieldVar.class));
        when(method.getMethodContext().getFields()).thenReturn(fields);
        doReturn(instanceFields).when(spyExecutor).getInstanceFields(fields);

        // when
        Instance instance = spyExecutor.prepareNewInstance(method);

        // then
        assertThat(instance.getText()).isEqualTo("instancja CLASS");
        assertThat(instance.getFields()).isSameAs(instanceFields);
    }

    @Test
    void shouldGetInstanceFields() {
        // given
        FieldVar staticField = mock(FieldVar.class);
        when(staticField.isStatic()).thenReturn(true);

        FieldVar field = mock(FieldVar.class);
        FieldVar instanceField = mock(FieldVar.class);
        when(cloner.deepClone(field)).thenReturn(instanceField);

        HashMap<String, FieldVar> nameToField = new HashMap<>();
        nameToField.put("F1", staticField);
        nameToField.put("F2", field);

        // when
        Map<String, FieldVar> instanceFields = executor.getInstanceFields(nameToField);

        // then
        assertThat(instanceFields).containsExactly(entry("F1", staticField), entry("F2", instanceField));
    }

    @Test
    void shouldReturnEmptyListWhenNullBodyContext() {
        // given
        Method method = mock(Method.class, RETURNS_DEEP_STUBS);
        when(method.getHeader().getArguments()).thenReturn(Collections.emptyList());
        when(method.getBodyContext()).thenReturn(null);
        CallInvocation invocation = mock(CallInvocation.class, RETURNS_DEEP_STUBS);

        // when
        List<Statement> statements = executor.executeInContext(method, invocation, context);

        // then
        assertThat(statements).isEmpty();
    }

    @Test
    void shouldInitializeInstanceFields() {
        FieldVar fieldVar = mock(FieldVar.class);
        FieldVar staticFieldVar = mock(FieldVar.class);
        when(staticFieldVar.isStatic()).thenReturn(true);
        Map<String, FieldVar> fields = ImmutableMap.<String, FieldVar>builder()
                .put("F1", fieldVar)
                .put("F2", staticFieldVar)
                .build();

        executor.initializeInstanceFields(context, fields);

        verify(fieldVar).isStatic();
        verify(fieldVar).initialize(context);
        verify(staticFieldVar).isStatic();
        verifyNoMoreInteractions(staticFieldVar);
    }
}