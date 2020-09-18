package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Statement;

import java.util.Objects;

public class MethodBodyStatementVisitor extends JavaVisitor<Statement> {
    private final String contextMethodName;

    public MethodBodyStatementVisitor(String contextMethodName) {
        this.contextMethodName = Objects.requireNonNull(contextMethodName, "contextMethodName cannot be null");
    }

    @Override
    public Statement visitMethodBodyStatement(JavaParser.MethodBodyStatementContext ctx) {
        return new MethodCallVisitor(contextMethodName).visit(ctx.methodCall());
    }
}