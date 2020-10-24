package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.Expression;

import java.util.Objects;

public class Assignment extends Statement {
    private final String id;
    private final Expression value;

    public Assignment(String text, String id, Expression value) {
        super(Objects.requireNonNull(text, "text cannot be null"));
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public String getId() {
        return id;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public String getResolved() {
        return id + '=' + value.getResolved();
    }
}