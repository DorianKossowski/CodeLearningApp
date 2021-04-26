package com.server.parser.java.variable;

import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Value;
import com.server.parser.java.value.util.ValuePreparer;
import com.server.parser.util.EmptyExpressionPreparer;

import java.util.Objects;

public class FieldVar extends Variable {
    private final FieldVarInitExpressionFunction initFunction;
    private final Value emptyValue = EmptyExpressionPreparer.prepare(getType()).getValue();

    // only for test purpose
    public FieldVar(String type, String name, FieldVarInitExpressionFunction initFunction, Value value) {
        super(type, name, Objects.requireNonNull(value, "value cannot be null"));
        this.initFunction = Objects.requireNonNull(initFunction, "initFunction cannot be null");
    }

    public FieldVar(FieldVarDef fieldVarDef) {
        super(fieldVarDef, null);
        this.initFunction = fieldVarDef.getInitFunction();
    }

    @Override
    public void initialize(JavaContext context) {
        if (value != null) {
            throw new UnsupportedOperationException(String.format("Field %s already initialized", getName()));
        }
        value = ValuePreparer.prepare(getType(), initFunction.apply(context));
    }

    @Override
    public Value getValue() {
        if (value == null) {
            return emptyValue;
        }
        return value;
    }

    public boolean isStatic() {
        return getModifiers().contains("static");
    }
}