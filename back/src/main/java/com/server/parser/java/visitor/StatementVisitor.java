package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.*;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatementVisitor extends JavaVisitor<Statement> {

    @Override
    public Statement visit(ParserRuleContext ctx, JavaContext context) {
        return new MethodBodyStatementVisitorInternal(context).visit(ctx);
    }

    private static class MethodBodyStatementVisitorInternal extends JavaBaseVisitor<Statement> {
        private final JavaContext context;

        private MethodBodyStatementVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        //*** METHOD CALL ***//
        @Override
        public MethodCall visitMethodCall(JavaParser.MethodCallContext ctx) {
            String methodName = textVisitor.visit(ctx.methodName());
            List<Expression> arguments = visit(ctx.callArguments());
            return new MethodCall(JavaGrammarHelper.getOriginalText(ctx), context.getCurrentMethodContext().getMethodName(), methodName,
                    arguments);
        }

        private List<Expression> visit(JavaParser.CallArgumentsContext ctx) {
            JavaVisitor<Expression> expressionVisitor = context.getVisitor(Expression.class);
            return ctx.expression().stream()
                    .map(expressionContext -> expressionVisitor.visit(expressionContext, context))
                    .collect(Collectors.toList());
        }

        //*** VARIABLE ***//
        @Override
        public Statement visitMethodVarDec(JavaParser.MethodVarDecContext ctx) {
            Variable variable = (Variable) visit(ctx.varDec());
            context.getCurrentMethodContext().addVar(variable.getName(), variable.getValue());
            return variable;
        }

        @Override
        public Statement visitFieldDec(JavaParser.FieldDecContext ctx) {
            return visit(ctx.varDec());
        }

        @Override
        public Statement visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = textVisitor.visit(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new Variable(JavaGrammarHelper.getOriginalText(ctx), type, id);
        }

        @Override
        public Statement visitVarDec(JavaParser.VarDecContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            Expression expression = null;
            if (ctx.expression() != null) {
                expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            }
            return new Variable(JavaGrammarHelper.getOriginalText(ctx), textVisitor.visit(ctx.type()), id, expression);
        }

        //*** ASSIGNMENT ***//
        @Override
        public Statement visitAssignment(JavaParser.AssignmentContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            Expression expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            context.getCurrentMethodContext().addVar(id, expression);
            return new Assignment(JavaGrammarHelper.getOriginalText(ctx), id, expression);
        }
    }
}