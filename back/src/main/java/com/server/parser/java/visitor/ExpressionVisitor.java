package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.ObjectRef;

public class ExpressionVisitor extends JavaVisitor<Expression> {

    @Override
    public Expression visitExpression(JavaParser.ExpressionContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        }
        if (ctx.objectRefName() != null) {
            return visit(ctx.objectRefName());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression visitLiteral(JavaParser.LiteralContext ctx) {
        return new LiteralVisitor().visit(ctx);
    }

    @Override
    public Expression visitObjectRefName(JavaParser.ObjectRefNameContext ctx) {
        return new ObjectRef(textVisitor.visit(ctx));
    }
}