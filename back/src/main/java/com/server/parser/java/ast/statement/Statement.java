package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import com.server.parser.java.ast.statement.property.PropertiesProvider;

import java.util.List;
import java.util.Objects;

public abstract class Statement extends PropertiesProvider implements AstElement {
    private final String text;

    protected Statement(String text) {
        this.text = Objects.requireNonNull(text, "text cannot be null");
    }

    public String getText() {
        return text;
    }

    public abstract List<ExpressionStatement> getExpressionStatements();
}