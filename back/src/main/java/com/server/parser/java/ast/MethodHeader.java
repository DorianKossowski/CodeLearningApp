package com.server.parser.java.ast;

import java.util.List;

public class MethodHeader extends AstElement {
    private final String result;
    private final String name;
    private final List<Variable> arguments;

    public MethodHeader(String result, String name, List<Variable> arguments) {
        this.result = result;
        this.name = name;
        this.arguments = arguments;
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
