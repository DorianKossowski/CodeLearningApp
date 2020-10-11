package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Statement;
import com.server.parser.java.context.MethodContext;

import java.util.Objects;

public class MethodBodyStatementVisitor extends JavaVisitor<Statement> {
    private final MethodContext methodContext;

    public MethodBodyStatementVisitor(MethodContext methodContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
    }

    @Override
    public Statement visitMethodBodyStatement(JavaParser.MethodBodyStatementContext ctx) {
        return new MethodCallVisitor(methodContext).visit(ctx.methodCall());
    }
}