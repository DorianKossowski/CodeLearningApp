package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.Statement;

import java.util.List;
import java.util.Objects;

public class Task implements AstElement {
    private final ClassAst classAst;
    private final List<Statement> calledStatements;
    private final List<PrintCallStatement> printCalls;

    public Task(ClassAst classAst, List<Statement> calledStatements, List<PrintCallStatement> printCalls) {
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

    public List<PrintCallStatement> getPrintCalls() {
        return printCalls;
    }
}