package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class ObjectRefExpressionVisitor extends JavaVisitor<ObjectRefExpression> {
    @Override
    public ObjectRefExpression visit(ParserRuleContext ctx, JavaContext context) {
        return new ObjectRefExpressionVisitorInternal(context).visit(ctx);
    }

    private static class ObjectRefExpressionVisitorInternal extends JavaBaseVisitor<ObjectRefExpression> {
        private final JavaContext context;

        public ObjectRefExpressionVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public ObjectRefExpression visitObjectRefName(JavaParser.ObjectRefNameContext ctx) {
            Value currentValue = resolveFirstSegment(ctx);
            for (JavaParser.IdentifierContext nextSegmentCtx : ctx.identifier()) {
                String nextSegmentName = textVisitor.visit(nextSegmentCtx);
                currentValue = currentValue.getAttribute(nextSegmentName);
            }
            return new ObjectRefExpression(textVisitor.visit(ctx), currentValue);
        }

        private Value resolveFirstSegment(JavaParser.ObjectRefNameContext ctx) {
            String firstSegmentName = textVisitor.visit(ctx.objectRefNameFirstSegment());
            return context.getVariable(firstSegmentName).getValue();
        }
    }
}