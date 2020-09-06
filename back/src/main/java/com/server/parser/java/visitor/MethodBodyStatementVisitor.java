package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Statement;

public class MethodBodyStatementVisitor extends JavaVisitor<Statement> {

    @Override
    public Statement visitMethodBodyStatement(JavaParser.MethodBodyStatementContext ctx) {
        return new MethodCallVisitor().visit(ctx.methodCall());
    }
}