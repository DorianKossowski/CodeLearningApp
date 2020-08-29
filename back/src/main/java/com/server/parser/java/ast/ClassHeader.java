package com.server.parser.java.ast;

import java.util.Objects;

public class ClassHeader extends AstElement {
    private final String name;

    public ClassHeader(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public String getName() {
        return name;
    }
}