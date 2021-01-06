package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.VariableDef;

import java.util.List;

public class ConstructorHeader extends MethodHeader {

    public ConstructorHeader(List<String> modifiers, String name, List<VariableDef> arguments) {
        super(modifiers, null, name, arguments);
    }
}