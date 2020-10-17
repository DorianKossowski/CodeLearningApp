package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.constant.TextConstant;

public class StringLiteral extends Literal {

    public StringLiteral(TextConstant<String> value) {
        super(value, '"' + value.toString() + '"');
    }

    @Override
    public Constant<?> getResolved() {
        return new TextConstant<>('"' + constant.toString() + '"');
    }
}