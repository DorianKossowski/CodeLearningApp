package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;

import java.util.Map;

public class FieldDecVisitor extends JavaVisitor<Variable> {

    @Override
    public Variable visitFieldDec(JavaParser.FieldDecContext ctx) {
        String type = textVisitor.visit(ctx.type());
        Map.Entry<String, Expression> varDecEntry = new VarDecVisitor().visit(ctx.varDec());
        return new Variable(type, varDecEntry.getKey(), varDecEntry.getValue());
    }
}