package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.Statement;

import java.util.List;
import java.util.Objects;

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
}