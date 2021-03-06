package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.context.ContextParameters;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Value;
import com.server.parser.util.exception.ResolvingException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
            for (JavaParser.ObjectRefNameNextSegmentContext nextSegmentCtx : ctx.objectRefNameNextSegment()) {
                currentValue = resolveNextSegment(currentValue, nextSegmentCtx);
            }
            return new ObjectRefExpression(textVisitor.visit(ctx), currentValue);
        }

        private Value resolveNextSegment(Value currentValue, JavaParser.ObjectRefNameNextSegmentContext nextSegmentCtx) {
            if (nextSegmentCtx.identifier() != null) {
                String nextSegmentName = textVisitor.visit(nextSegmentCtx.identifier());
                currentValue = currentValue.getAttribute(nextSegmentName);
            }
            return currentValue;
        }

        private Value resolveFirstSegment(JavaParser.ObjectRefNameFirstSegmentContext ctx) {
            if (ctx.THIS() != null) {
                return Optional.ofNullable(context.getThisValue())
                        .orElseThrow(() -> new ResolvingException("Nie można użyć słowa kluczowego this ze statycznego kontekstu"));
            }
            if (ctx.identifier() != null) {
                String firstSegmentName = textVisitor.visit(ctx);
                return context.getVariable(firstSegmentName).getValue();
            }
            if (ctx.callSegment() != null) {
                JavaParser.CallSegmentContext callSegmentContext = ctx.callSegment();
                CallReference reference = new CallReference(context.getThisValue(), callSegmentContext.identifier().getText());
                List<Expression> arguments = callSegmentContext.callArguments() == null ? Collections.emptyList() : visitArguments(callSegmentContext.callArguments());
                ContextParameters parameters = context.getParameters();
                CallInvocation invocation = new CallInvocation(JavaGrammarHelper.getOriginalText(ctx), parameters.getMethodName(),
                        reference, arguments);
                return parameters.getCallResolver().resolve(parameters.isStaticContext(), invocation).getResult().getValue();

            }
            throw new UnsupportedOperationException("Unsupported segment context: " +
                    Iterables.getOnlyElement(ctx.children).getClass().getSimpleName());
        }

        private List<Expression> visitArguments(JavaParser.CallArgumentsContext ctx) {
            JavaVisitor<Expression> expressionVisitor = context.getVisitor(Expression.class);
            return ctx.expression().stream()
                    .map(expressionContext -> expressionVisitor.visit(expressionContext, context))
                    .collect(Collectors.toList());
        }
    }
}