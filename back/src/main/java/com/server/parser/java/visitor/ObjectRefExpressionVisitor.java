package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.ObjectRefValueResolver;

import java.util.Objects;

public class ObjectRefExpressionVisitor extends JavaVisitor<ObjectRefExpression> {
    private final JavaContext context;

    public ObjectRefExpressionVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public ObjectRefExpression visitObjectRefName(JavaParser.ObjectRefNameContext ctx) {
        ObjectRefValueResolver resolver = new ObjectRefValueResolver(context);
        return new ObjectRefExpression(textVisitor.visit(ctx), resolver.resolveValue(ctx));
    }
}