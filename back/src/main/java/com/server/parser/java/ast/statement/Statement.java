package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.AstElement;

import java.util.List;
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

    public abstract List<ExpressionStatement> getExpressionStatements();
}