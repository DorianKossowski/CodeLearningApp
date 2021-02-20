package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;

import java.util.List;
import java.util.Objects;

public class Task extends AstElement {
    private final ClassAst classAst;
    private final List<Statement> calledStatements;
    private final List<CallStatement> printCalls;

    public Task(ClassAst classAst, List<Statement> calledStatements, List<CallStatement> printCalls) {
        this.classAst = Objects.requireNonNull(classAst, "classAst cannot be null");
        this.calledStatements = Objects.requireNonNull(calledStatements, "calledStatements cannot be null");
        this.printCalls = Objects.requireNonNull(printCalls, "printCalls cannot be null");
    }

    public ClassAst getClassAst() {
        return classAst;
    }

    public List<Statement> getCalledStatements() {
        return calledStatements;
    }

    public List<CallStatement> getPrintCalls() {
        return printCalls;
    }
}