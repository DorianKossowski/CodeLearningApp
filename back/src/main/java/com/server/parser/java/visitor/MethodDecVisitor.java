package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodBody;
import com.server.parser.java.ast.MethodHeader;

public class MethodDecVisitor extends JavaVisitor<Method> {

    @Override
    public Method visitMethodDec(JavaParser.MethodDecContext ctx) {
        MethodHeader methodHeader = new MethodHeaderVisitor().visit(ctx.methodHeader());
        MethodBody methodBody = new MethodBodyVisitor().visit(ctx.methodBody());
        return new Method(methodHeader, methodBody);
    }
}