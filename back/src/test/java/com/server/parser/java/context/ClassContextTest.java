package com.server.parser.java.context;

import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.FieldVarInitExpressionSupplier;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ClassContextTest {
    private static final String NAME = "name";

    private ClassContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new ClassContext();
    }

    @Test
    void shouldAddFieldVariable() {
        FieldVar variable = new FieldVar("", NAME, new FieldVarInitExpressionSupplier(() -> mock(Expression.class)), mock(Value.class));
        context.addField(variable);

        assertThat(context.getFields().get(NAME)).isSameAs(variable);
    }

    @Test
    void shouldThrowWhenAddFieldAgain() {
        FieldVar variable = new FieldVar("", NAME, new FieldVarInitExpressionSupplier(() -> mock(Expression.class)), mock(Value.class));
        context.addField(variable);

        assertThatThrownBy(() -> context.addField(variable))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Pole name już istnieje");
    }
}