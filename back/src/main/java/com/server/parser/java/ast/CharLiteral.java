package com.server.parser.java.ast;

public class CharLiteral extends Literal {

    public CharLiteral(Object value) {
        super(value, '\'' + value.toString() + '\'');
    }

    @Override
    public Object getResolved() {
        return '\'' + value.toString() + '\'';
    }
}