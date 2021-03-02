package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class VariableDef extends ExpressionStatement {
    private List<String> modifiers = new ArrayList<>();
    private final String type;
    private final String name;
    private final boolean explicitInitialization;

    VariableDef(String text, String type, String name, boolean explicitInitialization) {
        super(text);
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.explicitInitialization = explicitInitialization;
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

    @Override
    public String getResolved() {
        StringBuilder text = new StringBuilder().append(String.format("%s %s %s", String.join(" ", getModifiers()),
                getType(), getName()));
        if (explicitInitialization) {
            text.append(" = ").append(getValue());
        }
        return text.toString();
    }
}