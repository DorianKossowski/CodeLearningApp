package com.server.parser.java.ast;

public class Variable extends AstElement {
    private final String type;
    private final String name;

    public Variable(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}