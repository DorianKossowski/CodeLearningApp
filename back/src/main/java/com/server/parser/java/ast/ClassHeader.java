package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;

public class ClassHeader extends AstElement {
    private final List<String> modifiers;
    private final String name;

    public ClassHeader(List<String> modifiers, String name) {
        this.modifiers = Objects.requireNonNull(modifiers, "modifiers cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public String getName() {
        return name;
    }
}