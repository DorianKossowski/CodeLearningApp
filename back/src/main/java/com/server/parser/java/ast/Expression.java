package com.server.parser.java.ast;

import java.util.Objects;

public abstract class Expression extends AstElement {
    private final String text;

    public Expression(String text) {
        this.text = Objects.requireNonNull(text, "text cannot be null");
    }

    public String getText() {
        return text;
    }

    public abstract Object getResolved();
}