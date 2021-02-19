package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Statement extends AstElement {
    private final String text;
    private final Map<String, String> properties = new HashMap<>();

    protected Statement(String text) {
        this.text = Objects.requireNonNull(text, "text cannot be null");
    }

    public String getText() {
        return text;
    }

    //TODO move properties stuff to separate class
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public abstract List<ExpressionStatement> getExpressionStatements();
}