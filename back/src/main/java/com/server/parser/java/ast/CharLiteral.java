package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.constant.TextConstant;

public class CharLiteral extends Literal {

    public CharLiteral(TextConstant<Character> value) {
        super(value, '\'' + value.toString() + '\'');
    }

    @Override
    public Constant<?> getResolved() {
        return new TextConstant<>('\'' + constant.toString() + '\'');
    }
}