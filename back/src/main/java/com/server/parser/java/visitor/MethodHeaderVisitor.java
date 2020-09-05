package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;

import java.util.List;

public class MethodHeaderVisitor extends JavaVisitor<MethodHeader> {

    @Override
    public MethodHeader visitMethodHeader(JavaParser.MethodHeaderContext ctx) {
        String methodResult = ctx.methodResult().getText();
        String identifier = ctx.identifier().getText();
        List<Variable> args = new MethodArgsVisitor().visit(ctx.methodArgs());
        return new MethodHeader(methodResult, identifier, args);
    }
}