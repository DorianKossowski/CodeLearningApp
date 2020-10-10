package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.context.MethodContext;

import java.util.Objects;

public class MethodVarDecVisitor extends JavaVisitor<Variable> {
    private final MethodContext methodContext;

    public MethodVarDecVisitor(MethodContext methodContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
    }

    @Override
    public Variable visitMethodVarDec(JavaParser.MethodVarDecContext ctx) {
        Variable variable = new VarDecVisitor().visit(ctx.varDec());
        methodContext.addVar(variable.getName(), variable.getValue().getText());
        return variable;
    }
}