package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.ast.value.NullValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class FieldVarTest {

    @Test
    void shouldThrowWhenTwoTimesInit() {
        FieldVarDef fieldVarDef = new FieldVarDef("", "String", "name",
                new FieldVarInitExpressionSupplier(() -> new Literal(new StringConstant("str"))), false);
        FieldVar fieldVar = new FieldVar(fieldVarDef);
        fieldVar.initialize();

        assertThatThrownBy(fieldVar::initialize)
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Field name already initialized");
    }

    @Test
    void shouldGetEmptyValueBeforeInit() {
        FieldVarDef fieldVarDef = new FieldVarDef("", "String", "name",
                new FieldVarInitExpressionSupplier(() -> mock(Literal.class)), false);
        FieldVar fieldVar = new FieldVar(fieldVarDef);

        assertThat(fieldVar.getValue()).isSameAs(NullValue.INSTANCE);
    }
}