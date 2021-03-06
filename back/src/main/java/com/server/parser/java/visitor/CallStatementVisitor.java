package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Value;
import com.server.parser.java.visitor.resolver.ObjectRefResolver;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class CallStatementVisitor extends JavaVisitor<CallStatement> {

    @Override
    public CallStatement visit(ParserRuleContext ctx, JavaContext context) {
        return new CallStatementVisitorInternal(context).visit(ctx);
    }

    private static class CallStatementVisitorInternal extends JavaBaseVisitor<CallStatement> {
        private final JavaContext context;

        public CallStatementVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public CallStatement visitCall(JavaParser.CallContext ctx) {
            Value valueToCallOn = context.getThisValue();
            if (ctx.objectRefName() != null) {
                ObjectRefExpression objectRef = context.getVisitor(ObjectRefExpression.class).visit(ctx.objectRefName(), context);
                valueToCallOn = objectRef.getValue();
            }
            ObjectRefResolver objectRefResolver = new ObjectRefResolver(context);
            return objectRefResolver.resolveCall(valueToCallOn, ctx.callSegment());
        }
    }
}