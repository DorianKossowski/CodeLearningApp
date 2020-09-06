package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;

import java.util.List;
import java.util.stream.Collectors;

public class MethodNameVisitor extends JavaVisitor<String> {

    @Override
    public String visitMethodName(JavaParser.MethodNameContext ctx) {
        List<String> ids = ctx.identifier().stream()
                .map(textVisitor::visit)
                .collect(Collectors.toList());
        return JavaGrammarHelper.createMethodName(ids);
    }
}