package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;
import java.util.stream.Collectors;

public class MethodHeaderVisitor extends JavaVisitor<MethodHeader> {

    @Override
    public MethodHeader visitMethodHeader(JavaParser.MethodHeaderContext ctx) {
        List<String> modifiers = ctx.methodModifier().stream()
                .map(RuleContext::getText)
                .collect(Collectors.toList());
        String methodResult = ctx.methodResult().getText();
        String identifier = ctx.identifier().getText();
        List<Variable> args = new MethodArgsVisitor().visit(ctx.methodArgs());
        return new MethodHeader(modifiers, methodResult, identifier, args);
    }
}