package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.EmptyExpressionPreparer;
import com.server.parser.util.ValuePreparer;

import java.util.Objects;

public class FieldVar extends Variable {
    private final FieldVarInitExpressionSupplier initSupplier;
    private final Value emptyValue = EmptyExpressionPreparer.prepare(getType()).getValue();
    private Value value;

    // only for test purpose
    public FieldVar(String type, String name, FieldVarInitExpressionSupplier initSupplier, Value value) {
        super(type, name);
        this.initSupplier = Objects.requireNonNull(initSupplier, "initSupplier cannot be null");
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public FieldVar(FieldVarDef fieldVarDef) {
        super(fieldVarDef);
        this.initSupplier = fieldVarDef.getInitSupplier();
    }

    public void initialize() {
        if (value != null) {
            throw new UnsupportedOperationException(String.format("Field %s already initialized", getName()));
        }
        value = ValuePreparer.prepare(getType(), initSupplier.get());
    }

    @Override
    public Value getValue() {
        if (value == null) {
            return emptyValue;
        }
        return value;
    }

    @Override
    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isStatic() {
        return getModifiers().contains("static");
    }
}