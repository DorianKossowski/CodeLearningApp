package com.server.parser.java.variable;

import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Value;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

public class MethodVar extends Variable {

    // only for test purpose
    public MethodVar(String type, String name, Value value) {
        super(type, name, Objects.requireNonNull(value, "value cannot be null"));
    }

    public MethodVar(VariableDef methodVarDef) {
        super(methodVarDef, methodVarDef.getValue());
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public void initialize(JavaContext context) {
        throw new NotImplementedException();
    }
}