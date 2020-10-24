package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.VariableDef;

import java.util.List;
import java.util.Objects;

public class MethodHeader extends AstElement {
    private final List<String> modifiers;
    private final String result;
    private final String name;
    private final List<VariableDef> arguments;

    public MethodHeader(List<String> modifiers, String result, String name, List<VariableDef> arguments) {
        this.modifiers = Objects.requireNonNull(modifiers, "modifiers cannot be null");
        this.result = Objects.requireNonNull(result, "result cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.arguments = Objects.requireNonNull(arguments, "arguments cannot be null");
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public String getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

    public List<VariableDef> getArguments() {
        return arguments;
    }
}
