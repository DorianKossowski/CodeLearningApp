package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodBody;
import com.server.parser.java.ast.MethodStatement;
import com.server.parser.java.ast.Statement;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.context.MethodContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodBodyVisitor extends JavaVisitor<MethodBody> {
    private final MethodContext methodContext;

    public MethodBodyVisitor(MethodContext methodContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
    }

    @Override
    public MethodBody visitMethodBody(JavaParser.MethodBodyContext ctx) {
        MethodBodyStatementVisitor methodBodyStatementVisitor = new MethodBodyStatementVisitor(methodContext);
        Stream<MethodStatement> methodBodyStream = ctx.methodBodyStatement().stream()
                .map(methodBodyStatementVisitor::visit);
        LocalVarDecVisitor localVarDecVisitor = new LocalVarDecVisitor();
        Stream<Variable> variableStream = ctx.localVarDec().stream()
                .map(localVarDecVisitor::visit);

        List<Statement> statements = Stream.concat(methodBodyStream, variableStream).collect(Collectors.toList());
        return new MethodBody(statements);
    }
}