package com.server.parser.java.ast;

import java.util.Objects;

public class Expression extends AstElement {
    private final String text;

    public Expression(String text) {
        this.text = Objects.requireNonNull(text, "text cannot be null");
    }

    public String getText() {
        return text;
    }

    public String getResolved() {
        return '"' + text + '"';
    }
}