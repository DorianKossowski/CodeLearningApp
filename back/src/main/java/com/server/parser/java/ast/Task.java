package com.server.parser.java.ast;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.statement.MethodCall;
import com.server.parser.java.ast.statement.Statement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Task extends AstElement {
    private final ClassAst classAst;
    private final List<Statement> calledStatements;

    public Task(ClassAst classAst, List<Statement> calledStatements) {
        this.classAst = Objects.requireNonNull(classAst, "classAst cannot be null");
        this.calledStatements = Objects.requireNonNull(calledStatements, "calledStatements cannot be null");
    }

    public ClassAst getClassAst() {
        return classAst;
    }

    public List<Statement> getCalledStatements() {
        return calledStatements;
    }

    public String getOutput() {
        List<MethodCall> methodCalls = calledStatements.stream()
                .filter(statement -> statement instanceof MethodCall)
                .map(statement -> (MethodCall) statement)
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (MethodCall call : methodCalls) {
            if (call.getName().startsWith("System.out.print")) {
                sb.append(Iterables.getOnlyElement(call.getArgs()).getResolvedText());
                if (call.getName().equals("System.out.println")) {
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}