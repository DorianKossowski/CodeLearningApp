package com.server.parser.java.ast;

import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.ast.value.NullValue;
import com.server.parser.java.constant.StringConstant;
import com.server.parser.java.context.JavaContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class FieldVarTest {
    @Mock
    private JavaContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldThrowWhenTwoTimesInit() {
        FieldVarDef fieldVarDef = createFieldVarDef(new Literal(new StringConstant("str")));
        FieldVar fieldVar = new FieldVar(fieldVarDef);
        fieldVar.initialize(context);

        assertThatThrownBy(() -> fieldVar.initialize(context))
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Field name already initialized");
    }

    private FieldVarDef createFieldVarDef(Literal literal) {
        return new FieldVarDef("", "String", "name",
                new FieldVarInitExpressionFunction("", $ -> literal), false);
    }

    @Test
    void shouldGetEmptyValueBeforeInit() {
        FieldVarDef fieldVarDef = createFieldVarDef(mock(Literal.class));
        FieldVar fieldVar = new FieldVar(fieldVarDef);

        assertThat(fieldVar.getValue()).isSameAs(NullValue.INSTANCE);
    }
}