package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

public class MethodVar extends Variable {
    private Value value;

    // only for test purpose
    public MethodVar(String type, String name, Value value) {
        super(type, name);
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public MethodVar(VariableDef methodVarDef) {
        super(methodVarDef);
        this.value = methodVarDef.getValue();
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public void initialize(JavaContext context) {
        throw new NotImplementedException();
    }
}