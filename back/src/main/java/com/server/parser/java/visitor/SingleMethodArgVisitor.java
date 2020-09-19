package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;

public class SingleMethodArgVisitor extends JavaVisitor<Variable> {

    @Override
    public Variable visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
        String type = textVisitor.visit(ctx.type());
        String id = textVisitor.visit(ctx.identifier());
        return new Variable(JavaGrammarHelper.getOriginalText(ctx), type, id);
    }
}