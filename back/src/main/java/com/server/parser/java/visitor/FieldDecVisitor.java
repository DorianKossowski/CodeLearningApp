package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;

public class FieldDecVisitor extends JavaVisitor<Variable> {

    @Override
    public Variable visitFieldDec(JavaParser.FieldDecContext ctx) {
        return new VarDecVisitor().visit(ctx.varDec());
    }
}