package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodBody;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.context.MethodContext;

import java.util.Objects;

public class MethodDecVisitor extends JavaVisitor<Method> {
    private final String className;

    public MethodDecVisitor(String className) {
        this.className = Objects.requireNonNull(className, "className cannot be null");
    }

    @Override
    public Method visitMethodDec(JavaParser.MethodDecContext ctx) {
        MethodHeader methodHeader = new MethodHeaderVisitor().visit(ctx.methodHeader());
        MethodContext methodContext = new MethodContext(methodHeader.getName());
        MethodBody methodBody = new MethodBodyVisitor(methodContext).visit(ctx.methodBody());
        return new Method(className, methodHeader, methodBody);
    }
}