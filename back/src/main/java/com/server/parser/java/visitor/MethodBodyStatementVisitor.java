package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodStatement;

import java.util.Objects;

public class MethodBodyStatementVisitor extends JavaVisitor<MethodStatement> {
    private final String contextMethodName;

    public MethodBodyStatementVisitor(String contextMethodName) {
        this.contextMethodName = Objects.requireNonNull(contextMethodName, "contextMethodName cannot be null");
    }

    @Override
    public MethodStatement visitMethodBodyStatement(JavaParser.MethodBodyStatementContext ctx) {
        return new MethodCallVisitor(contextMethodName).visit(ctx.methodCall());
    }
}