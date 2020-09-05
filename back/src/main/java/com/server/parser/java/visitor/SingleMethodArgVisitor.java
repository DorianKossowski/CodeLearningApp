package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;

public class SingleMethodArgVisitor extends JavaVisitor<Variable> {

    @Override
    public Variable visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
        String type = textVisitor.visit(ctx.type());
        String id = textVisitor.visit(ctx.identifier());
        return new Variable(type, id);
    }
}