package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Value;
import com.server.parser.java.visitor.resolver.ObjectRefValueResolver;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class CallStatementVisitor extends JavaVisitor<CallStatement> {
    private final JavaContext context;

    public CallStatementVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public CallStatement visit(ParserRuleContext ctx, JavaContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CallStatement visitCallStatement(JavaParser.CallStatementContext ctx) {
        Value valueToCallOn = context.getThisValue();
        ObjectRefValueResolver objectRefValueResolver = new ObjectRefValueResolver(context);
        if (ctx.objectRefName() != null) {
            valueToCallOn = objectRefValueResolver.resolveValue(ctx.objectRefName());
        }
        return objectRefValueResolver.resolveCall(valueToCallOn, ctx.callSegment());
    }
}