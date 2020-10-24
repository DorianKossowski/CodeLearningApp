package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.Value;
import com.server.parser.java.ast.constant.Constant;

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

    public abstract Constant<?> getResolved();

    public abstract Value getValue();

    public abstract Constant<?> getConstant();
}