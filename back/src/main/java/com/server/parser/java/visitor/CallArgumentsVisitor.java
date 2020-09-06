package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;

import java.util.List;
import java.util.stream.Collectors;

public class CallArgumentsVisitor extends JavaVisitor<List<Expression>> {

    @Override
    public List<Expression> visitCallArguments(JavaParser.CallArgumentsContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor();
        return ctx.expression().stream()
                .map(expressionVisitor::visit)
                .collect(Collectors.toList());
    }
}