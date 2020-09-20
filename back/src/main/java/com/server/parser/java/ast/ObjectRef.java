package com.server.parser.java.ast;

public class ObjectRef extends Expression {
    private String value;

    public ObjectRef(String text) {
        super(text);
    }

    @Override
    public String getResolved() {
        return '"' + value + '"';
    }

    public void setValue(String value) {
        this.value = value;
    }
}