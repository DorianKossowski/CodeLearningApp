package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;

public class VarDecVisitor extends JavaVisitor<Variable> {

    @Override
    public Variable visitVarDec(JavaParser.VarDecContext ctx) {
        String id = textVisitor.visit(ctx.identifier());
        Expression expression = null;
        if (ctx.expression() != null) {
            expression = new ExpressionVisitor().visit(ctx.expression());
        }
        return new Variable(JavaGrammarHelper.getOriginalText(ctx), textVisitor.visit(ctx.type()), id, expression);
    }
}