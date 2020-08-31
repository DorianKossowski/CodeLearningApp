package com.server.parser.java.ast;

public class Expression extends AstElement {
    private final String text;

    public Expression(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}