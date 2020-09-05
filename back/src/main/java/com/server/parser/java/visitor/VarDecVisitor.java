package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;

import java.util.AbstractMap;
import java.util.Map;

public class VarDecVisitor extends JavaVisitor<Map.Entry<String, Expression>> {

    @Override
    public Map.Entry<String, Expression> visitVarDec(JavaParser.VarDecContext ctx) {
        String id = textVisitor.visit(ctx.identifier());
        Expression expression = null;
        if (ctx.expression() != null) {
            expression = new ExpressionVisitor().visit(ctx.expression());
        }
        return new AbstractMap.SimpleEntry<>(id, expression);
    }
}