package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.FieldVarInitExpressionSupplier;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.ValuePreparer;

import java.util.Objects;

public class FieldVarDef extends VariableDef {
    private final FieldVarInitExpressionSupplier initSupplier;

    public FieldVarDef(String text, String type, String name, FieldVarInitExpressionSupplier initSupplier,
                       boolean explicitInitialization) {
        super(text, type, name, explicitInitialization);
        this.initSupplier = Objects.requireNonNull(initSupplier, "initSupplier cannot be null");
    }

    @Override
    public Value getValue() {
        Expression expression = initSupplier.get();
        return ValuePreparer.prepare(getType(), expression);
    }

    public FieldVarInitExpressionSupplier getInitSupplier() {
        return initSupplier;
    }
}