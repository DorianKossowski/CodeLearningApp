package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.TextConstant;

public class StringLiteral extends Literal {

    public StringLiteral(TextConstant value) {
        super(value, '"' + value.toString() + '"');
    }

    @Override
    public Object getResolved() {
        return '"' + value.toString() + '"';
    }
}