package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;

public class MethodHeader extends AstElement {
    private final String result;
    private final String name;
    private final List<Variable> arguments;

    public MethodHeader(String result, String name, List<Variable> arguments) {
        this.result = Objects.requireNonNull(result, "result cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.arguments = Objects.requireNonNull(arguments, "arguments cannot be null");
    }

    public String getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getArguments() {
        return arguments;
    }
}
