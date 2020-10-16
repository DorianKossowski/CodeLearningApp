package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.TextConstant;

public class CharLiteral extends Literal {

    public CharLiteral(TextConstant value) {
        super(value, '\'' + value.toString() + '\'');
    }

    @Override
    public Object getResolved() {
        return '\'' + constant.toString() + '\'';
    }
}