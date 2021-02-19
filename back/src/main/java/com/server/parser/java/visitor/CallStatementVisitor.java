package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.CallReference;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            CallReference callReference = visitCallReference(ctx.callName());
            List<Expression> arguments = ctx.callArguments() == null ? Collections.emptyList() : visitArguments(ctx.callArguments());
            CallInvocation invocation = new CallInvocation(JavaGrammarHelper.getOriginalText(ctx), context.getMethodName(),
                    callReference, arguments);
            return context.getCallHandler().execute(invocation);
        }

        CallReference visitCallReference(JavaParser.CallNameContext ctx) {
            if (ctx.specialCallName() != null) {
                return new CallReference(ctx.specialCallName().getText());
            }
            String firstSegment = ctx.firstSeg.getText();
            if (ctx.secSeg != null) {
                return new CallReference(context.getVariable(firstSegment), ctx.secSeg.getText());
            }
            return new CallReference(firstSegment);
        }

        private List<Expression> visitArguments(JavaParser.CallArgumentsContext ctx) {
            JavaVisitor<Expression> expressionVisitor = context.getVisitor(Expression.class);
            return ctx.expression().stream()
                    .map(expressionContext -> expressionVisitor.visit(expressionContext, context))
                    .collect(Collectors.toList());
        }
    }
}