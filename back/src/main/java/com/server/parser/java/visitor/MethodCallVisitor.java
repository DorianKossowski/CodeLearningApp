package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.MethodCall;
import com.server.parser.java.context.MethodContext;

import java.util.List;
import java.util.Objects;

public class MethodCallVisitor extends JavaVisitor<MethodCall> {
    private final MethodContext methodContext;

    public MethodCallVisitor(MethodContext methodContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
    }

    @Override
    public MethodCall visitMethodCall(JavaParser.MethodCallContext ctx) {
        String methodName = new MethodNameVisitor().visit(ctx.methodName());
        List<Expression> arguments = new CallArgumentsVisitor().visit(ctx.callArguments());
        return new MethodCall(JavaGrammarHelper.getOriginalText(ctx), methodContext.getMethodName(), methodName, arguments);
    }
}