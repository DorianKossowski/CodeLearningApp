package com.server.parser.java.context;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.value.Value;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.FieldVarInitExpressionFunction;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ClassContextTest {
    private static final String TYPE = "int";
    private static final String NAME = "name";

    private ClassContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new ClassContext();
    }

    @Test
    void shouldAddFieldVariable() {
        FieldVar variable = new FieldVar(TYPE, NAME, createDummyInitFunction(), mock(Value.class));
        context.addField(variable);

        assertThat(context.getFields().get(NAME)).isSameAs(variable);
    }

    private FieldVarInitExpressionFunction createDummyInitFunction() {
        return new FieldVarInitExpressionFunction("", $ -> mock(Expression.class));
    }

    @Test
    void shouldThrowWhenAddFieldAgain() {
        FieldVar variable = new FieldVar(TYPE, NAME, createDummyInitFunction(), mock(Value.class));
        context.addField(variable);

        assertThatThrownBy(() -> context.addField(variable))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Pole name już istnieje");
    }
}