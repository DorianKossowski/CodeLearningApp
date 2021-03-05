package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.value.Value;

import java.util.Objects;

public abstract class Expression extends AstElement {
    private final String text;

    public Expression(String text) {
        this.text = Objects.requireNonNull(text, "text cannot be null");
    }

    public String getText() {
        return text;
    }

    public abstract Literal getLiteral();

    public abstract String getResolvedText();

    public abstract String getOutput();

    public abstract Value getValue();
}