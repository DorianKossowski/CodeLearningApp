package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.util.VariableDefPreparer;

import java.util.Objects;

public class VariableDef extends Statement {
    private final String type;
    private final String name;
    private final Expression value;

    public VariableDef(String text, String type, String name) {
        this(text, type, name, null);
    }

    public VariableDef(String text, String type, String name, Expression value) {
        super(text);
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        VariableDefPreparer.prepare(type, value);
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public String getResolved() {
        return String.format("%s %s = %s", type, name, value.getResolved());
    }
}