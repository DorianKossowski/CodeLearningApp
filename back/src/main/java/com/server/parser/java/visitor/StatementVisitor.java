package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Assignment;
import com.server.parser.java.ast.statement.MethodCall;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.EmptyExpressionPreparer;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
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
        public MethodCall visitCall(JavaParser.CallContext ctx) {
            String methodName = textVisitor.visit(ctx.callName());
            List<Expression> arguments;
            arguments = ctx.callArguments() == null ? Collections.emptyList() : visit(ctx.callArguments());
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
            VariableDef variableDef = (VariableDef) visit(ctx.varDec());
            context.getCurrentMethodContext().addVar(new Variable(variableDef));
            return variableDef;
        }

        @Override
        public Statement visitFieldDec(JavaParser.FieldDecContext ctx) {
            return visit(ctx.varDec());
        }

        @Override
        public Statement visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = textVisitor.visit(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id);
        }

        @Override
        public Statement visitVarDec(JavaParser.VarDecContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            String type = textVisitor.visit(ctx.type());
            Expression expression;
            if (ctx.expression() != null) {
                expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            } else {
                expression = EmptyExpressionPreparer.prepare(type);
            }
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id, expression);
        }

        //*** ASSIGNMENT ***//
        @Override
        public Statement visitAssignment(JavaParser.AssignmentContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            Expression expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            context.getCurrentMethodContext().updateVar(id, expression);
            return new Assignment(JavaGrammarHelper.getOriginalText(ctx), id, expression);
        }
    }
}