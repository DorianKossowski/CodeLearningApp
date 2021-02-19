package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class VariableDef extends ExpressionStatement {
    private List<String> modifiers = new ArrayList<>();
    private final String type;
    private final String name;

    VariableDef(String text, String type, String name) {
        super(text);
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
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
}