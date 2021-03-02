package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.FieldVarInitExpressionFunction;
import com.server.parser.java.ast.value.Value;

import java.util.Objects;

public class FieldVarDef extends VariableDef {
    private final FieldVarInitExpressionFunction initFunction;

    public FieldVarDef(String text, String type, String name, FieldVarInitExpressionFunction initFunction,
                       boolean explicitInitialization) {
        super(text, type, name, explicitInitialization);
        this.initFunction = Objects.requireNonNull(initFunction, "initFunction cannot be null");
    }

    @Override
    public Value getValue() {
        throw new UnsupportedOperationException("Can't directly reference to field value in FieldVarDef");
    }

    public FieldVarInitExpressionFunction getInitFunction() {
        return initFunction;
    }
}