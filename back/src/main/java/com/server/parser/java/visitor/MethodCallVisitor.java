package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.MethodCall;

import java.util.List;

public class MethodCallVisitor extends JavaVisitor<MethodCall> {

    @Override
    public MethodCall visitMethodCall(JavaParser.MethodCallContext ctx) {
        String methodName = new MethodNameVisitor().visit(ctx.methodName());
        List<Expression> arguments = new CallArgumentsVisitor().visit(ctx.callArguments());
        return new MethodCall(methodName, arguments);
    }
}