package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;

import java.util.List;
import java.util.stream.Collectors;

public class MethodArgsVisitor extends JavaVisitor<List<Variable>> {

    @Override
    public List<Variable> visitMethodArgs(JavaParser.MethodArgsContext ctx) {
        SingleMethodArgVisitor singleMethodArgVisitor = new SingleMethodArgVisitor();
        return ctx.singleMethodArg().stream()
                .map(singleMethodArgVisitor::visit)
                .collect(Collectors.toList());
    }
}