package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.context.MethodContext;

import java.util.Objects;

public class LocalVarDecVisitor extends JavaVisitor<Variable> {
    private final MethodContext methodContext;

    public LocalVarDecVisitor(MethodContext methodContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
    }

    @Override
    public Variable visitLocalVarDec(JavaParser.LocalVarDecContext ctx) {
        Variable variable = new VarDecVisitor().visit(ctx.varDec());
        methodContext.addVar(variable.getName(), variable.getValue().getText());
        return variable;
    }
}