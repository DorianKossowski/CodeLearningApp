package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.ConstructorCallReference;
import com.server.parser.java.call.reference.PrintCallReference;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.*;
import com.server.parser.util.exception.ResolvingException;
import com.server.parser.util.exception.ResolvingNullPointerException;
import com.server.parser.util.exception.ResolvingUninitializedException;
import com.server.parser.util.exception.ResolvingVoidException;
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
            return context.getCallResolver().resolve(context.isStaticContext(), invocation);
        }

        CallReference visitCallReference(JavaParser.CallNameContext ctx) {
            if (ctx.specialPrintCallName() != null) {
                return new PrintCallReference(ctx.specialPrintCallName().getText());
            }
            if (ctx.constructorCallName() != null) {
                return new ConstructorCallReference(ctx.constructorCallName().classSeg.getText());
            }
            if (ctx.objectRefName() != null) {
                ObjectRefExpression objectRef = context.getVisitor(ObjectRefExpression.class).visit(ctx.objectRefName(), context);
                Value value = objectRef.getValue();
                validateValueToCallOn(objectRef, value);
                return new CallReference((ObjectValue) value, ctx.methodName.getText());
            }
            return new CallReference(context.getThisValue(), ctx.methodName.getText());
        }

        private void validateValueToCallOn(ObjectRefExpression objectRef, Value value) {
            if (value instanceof ObjectValue) {
                return;
            }
            if (value instanceof PrimitiveValue) {
                throw new ResolvingException("Nie można wywołać metody na prymitywie");
            }
            if (value instanceof NullValue) {
                throw new ResolvingNullPointerException();
            }
            if (value instanceof VoidValue) {
                throw new ResolvingVoidException();
            }
            if (value instanceof UninitializedValue) {
                throw new ResolvingUninitializedException(objectRef.getResolvedText());
            }
            throw new UnsupportedOperationException();
        }

        private List<Expression> visitArguments(JavaParser.CallArgumentsContext ctx) {
            JavaVisitor<Expression> expressionVisitor = context.getVisitor(Expression.class);
            return ctx.expression().stream()
                    .map(expressionContext -> expressionVisitor.visit(expressionContext, context))
                    .collect(Collectors.toList());
        }
    }
}