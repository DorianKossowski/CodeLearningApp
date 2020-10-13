package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.ObjectRef;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class ExpressionVisitor extends JavaVisitor<Expression> {

    @Override
    public Expression visit(ParserRuleContext ctx, JavaContext context) {
        return new ExpressionVisitorInternal(context).visit(ctx);
    }

    private static class ExpressionVisitorInternal extends JavaBaseVisitor<Expression> {
        private final JavaContext context;

        public ExpressionVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

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
            return context.getVisitor(Literal.class).visit(ctx, context);
        }

        @Override
        public Expression visitObjectRefName(JavaParser.ObjectRefNameContext ctx) {
            String text = textVisitor.visit(ctx);
            return new ObjectRef(text, context.getCurrentMethodContext().getValue(text));
        }
    }
}