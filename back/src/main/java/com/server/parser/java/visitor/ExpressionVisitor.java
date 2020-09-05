package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;

public class ExpressionVisitor extends JavaVisitor<Expression> {

    @Override
    public Expression visitExpression(JavaParser.ExpressionContext ctx) {
        String literal = new LiteralVisitor().visit(ctx.literal());
        return new Expression(literal);
    }
}