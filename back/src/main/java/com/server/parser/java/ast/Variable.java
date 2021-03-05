package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Variable {
    private final List<String> modifiers;
    private final String type;
    private final String name;

    // only for test purpose
    Variable(String type, String name) {
        this.modifiers = Collections.emptyList();
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    Variable(VariableDef variableDef) {
        this.modifiers = Objects.requireNonNull(variableDef, "variableDef cannot be null").getModifiers();
        this.type = variableDef.getType();
        this.name = variableDef.getName();
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public abstract Value getValue();

    public abstract void setValue(Value value);

    public abstract void initialize(JavaContext context);
}