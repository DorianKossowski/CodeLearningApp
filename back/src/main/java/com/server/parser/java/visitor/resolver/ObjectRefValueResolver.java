package com.server.parser.java.visitor.resolver;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.ConstructorCallReference;
import com.server.parser.java.call.reference.PrintCallReference;
import com.server.parser.java.context.ContextParameters;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.*;
import com.server.parser.java.visitor.JavaDefaultTextVisitor;
import com.server.parser.util.exception.ResolvingException;
import com.server.parser.util.exception.ResolvingNullPointerException;
import com.server.parser.util.exception.ResolvingUninitializedException;
import com.server.parser.util.exception.ResolvingVoidException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectRefValueResolver {
    private static final JavaDefaultTextVisitor textVisitor = new JavaDefaultTextVisitor();

    private final JavaContext context;

    public ObjectRefValueResolver(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    public Value resolveValue(JavaParser.ObjectRefNameContext ctx) {
        Value currentValue = resolveFirstSegment(ctx.objectRefNameFirstSegment());
        for (JavaParser.ObjectRefNameNextSegmentContext nextSegmentCtx : ctx.objectRefNameNextSegment()) {
            currentValue = resolveNextSegment(currentValue, nextSegmentCtx);
        }
        return currentValue;
    }

    private Value resolveNextSegment(Value currentValue, JavaParser.ObjectRefNameNextSegmentContext ctx) {
        if (ctx.identifier() != null) {
            String nextSegmentName = textVisitor.visit(ctx.identifier());
            return currentValue.getAttribute(nextSegmentName);
        }
        if (ctx.callSegment() != null) {
            CallStatement callStatement = resolveCall(currentValue, ctx.callSegment());
            return callStatement.getResult().getValue();
        }
        throw new UnsupportedOperationException("Unsupported segment context: " +
                Iterables.getOnlyElement(ctx.children).getClass().getSimpleName());
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
            CallStatement callStatement = resolveCall(context.getThisValue(), ctx.callSegment());
            return callStatement.getResult().getValue();
        }
        throw new UnsupportedOperationException("Unsupported segment context: " +
                Iterables.getOnlyElement(ctx.children).getClass().getSimpleName());
    }

    public CallStatement resolveCall(Value valueToCallOn, JavaParser.CallSegmentContext ctx) {
        validateValueToCallOn(valueToCallOn);
        CallReference reference = resolveCallReference((ObjectValue) valueToCallOn, ctx.callName());
        List<Expression> arguments = ctx.callArguments() == null ? Collections.emptyList() : visitArguments(ctx.callArguments());
        ContextParameters parameters = context.getParameters();
        CallInvocation invocation = new CallInvocation(JavaGrammarHelper.getOriginalText(ctx), parameters.getMethodName(),
                reference, arguments);
        return parameters.getCallResolver().resolve(parameters.isStaticContext(), invocation);
    }

    void validateValueToCallOn(Value value) {
        if (value == null) {
            // static call
            return;
        }
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
            throw new ResolvingUninitializedException(value.getExpression().getResolvedText());
        }
        throw new UnsupportedOperationException();
    }

    CallReference resolveCallReference(ObjectValue valueToCallOn, JavaParser.CallNameContext ctx) {
        if (ctx.specialPrintCallName() != null) {
            return new PrintCallReference(ctx.specialPrintCallName().getText());
        }
        if (ctx.constructorCallName() != null) {
            return new ConstructorCallReference(ctx.constructorCallName().classSeg.getText());
        }
        return new CallReference(valueToCallOn, ctx.methodName.getText());
    }

    private List<Expression> visitArguments(JavaParser.CallArgumentsContext ctx) {
        return ctx.expression().stream()
                .map(context::resolveExpression)
                .collect(Collectors.toList());
    }
}
