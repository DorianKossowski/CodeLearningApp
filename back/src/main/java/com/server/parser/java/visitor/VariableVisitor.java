package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class VariableVisitor extends JavaVisitor<Variable> {

    @Override
    public Variable visit(ParserRuleContext ctx, JavaContext context) {
        return new VariableVisitorInternal(context).visit(ctx);
    }

    private static class VariableVisitorInternal extends JavaBaseVisitor<Variable> {
        private final JavaContext context;

        public VariableVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public Variable visitMethodVarDec(JavaParser.MethodVarDecContext ctx) {
            Variable variable = visit(ctx.varDec());
            context.getCurrentMethodContext().addVar(variable.getName(), variable.getValue().getText());
            return variable;
        }

        @Override
        public Variable visitFieldDec(JavaParser.FieldDecContext ctx) {
            return visit(ctx.varDec());
        }

        @Override
        public Variable visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = textVisitor.visit(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new Variable(JavaGrammarHelper.getOriginalText(ctx), type, id);
        }

        @Override
        public Variable visitVarDec(JavaParser.VarDecContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            Expression expression = null;
            if (ctx.expression() != null) {
                expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            }
            return new Variable(JavaGrammarHelper.getOriginalText(ctx), textVisitor.visit(ctx.type()), id, expression);
        }
    }
}