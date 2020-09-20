package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.context.MethodContext;

import java.util.Map;
import java.util.Objects;

public class LocalVarDecVisitor extends JavaVisitor<Variable> {
    private final MethodContext methodContext;

    public LocalVarDecVisitor(MethodContext methodContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
    }

    @Override
    public Variable visitLocalVarDec(JavaParser.LocalVarDecContext ctx) {
        String type = textVisitor.visit(ctx.type());
        Map.Entry<String, Expression> varDecEntry = new VarDecVisitor().visit(ctx.varDec());
        methodContext.addVar(varDecEntry.getKey(), varDecEntry.getValue().getText());
        return new Variable(JavaGrammarHelper.getOriginalText(ctx), type, varDecEntry.getKey(), varDecEntry.getValue());
    }
}