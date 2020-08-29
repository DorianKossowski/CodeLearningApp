package com.server.parser.java.ast;

import java.util.Objects;

public class Exercise extends AstElement {
    private final ClassAst classAst;

    public Exercise(ClassAst classAst) {
        this.classAst = Objects.requireNonNull(classAst, "classAst cannot be null");
    }

    public ClassAst getClassAst() {
        return classAst;
    }
}