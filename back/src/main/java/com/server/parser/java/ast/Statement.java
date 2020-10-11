package com.server.parser.java.ast;

import java.util.Objects;

public abstract class Statement extends AstElement {
    private final String text;

    protected Statement(String text) {
        this.text = Objects.requireNonNull(text, "text cannot be null");
    }

    public String getText() {
        return text;
    }

    public String getResolved() {
        throw new UnsupportedOperationException("Resolving not supported for type " + getClass());
    }
}