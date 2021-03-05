package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;
import java.util.Optional;

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
            Value currentValue = resolveFirstSegment(ctx.objectRefNameFirstSegment());
            for (JavaParser.IdentifierContext nextSegmentCtx : ctx.identifier()) {
                String nextSegmentName = textVisitor.visit(nextSegmentCtx);
                currentValue = currentValue.getAttribute(nextSegmentName);
            }
            return new ObjectRefExpression(textVisitor.visit(ctx), currentValue);
        }

        private Value resolveFirstSegment(JavaParser.ObjectRefNameFirstSegmentContext ctx) {
            if (ctx.THIS() != null) {
                return Optional.ofNullable(context.getThisValue())
                        .orElseThrow(() -> new ResolvingException("Nie można użyć słowa kluczowego this ze statycznego kontekstu"));
            }
            String firstSegmentName = textVisitor.visit(ctx);
            return context.getVariable(firstSegmentName).getValue();
        }
    }
}