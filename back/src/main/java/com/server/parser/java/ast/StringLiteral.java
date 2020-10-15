package com.server.parser.java.ast;

public class StringLiteral extends Literal {

    public StringLiteral(Object value) {
        super(value, '"' + value.toString() + '"');
    }

    @Override
    public Object getResolved() {
        return '"' + value.toString() + '"';
    }
}