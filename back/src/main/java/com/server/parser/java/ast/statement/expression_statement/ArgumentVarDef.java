package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.value.Value;

public class ArgumentVarDef extends VariableDef {

    public ArgumentVarDef(String text, String type, String name) {
        super(text, type, name, false);
    }

    @Override
    public Value getValue() {
        return null;
    }
}