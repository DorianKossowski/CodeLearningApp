package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.*;
import com.server.parser.util.EmptyExpressionPreparer;
import com.server.parser.util.exception.BreakStatementException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatementVisitor extends JavaVisitor<Statement> {

    @Override
    public Statement visit(ParserRuleContext ctx, JavaContext context) {
        try {
            return new StatementVisitorInternal(context).visit(ctx);
        } catch (BreakStatementException e) {
            if (isValidBreak(ctx)) {
                return BreakStatement.INSTANCE;
            }
            throw e;
        }
    }

    private static boolean isValidBreak(ParserRuleContext ctx) {
        ParserRuleContext parentContext = ctx.getParent();
        while (parentContext != null) {
            if (parentContext instanceof JavaParser.SwitchElementContext
                    || parentContext instanceof JavaParser.ForStatementContext
                    || parentContext instanceof JavaParser.WhileStatementContext
                    || parentContext instanceof JavaParser.DoWhileStatementContext) {
                return true;
            }
            parentContext = parentContext.getParent();
        }
        return false;
    }

    public static class StatementVisitorInternal extends JavaBaseVisitor<Statement> {
        private final JavaContext context;

        private StatementVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public Statement visitBlockStatement(JavaParser.BlockStatementContext ctx) {
            return new BlockStatement(visitBlockContentStatements(ctx));
        }

        private List<Statement> visitBlockContentStatements(JavaParser.BlockStatementContext ctx) {
            StatementVisitorInternal localVisitor = new StatementVisitorInternal(context.createLocalContext());
            List<Statement> statements = new ArrayList<>();
            for (JavaParser.StatementContext statementContext : ctx.statementList().statement()) {
                try {
                    Statement statement = localVisitor.visit(statementContext);
                    statements.add(statement);
                    if (statement.hasBreak()) {
                        return statements;
                    }
                } catch (BreakStatementException e) {
                    if (isValidBreak(ctx)) {
                        statements.add(BreakStatement.INSTANCE);
                        return statements;
                    }
                    throw e;
                }
            }
            return statements;
        }

        @Override
        public Statement visitExpressionStatement(JavaParser.ExpressionStatementContext ctx) {
            return visit(ctx.getChild(0));
        }

        @Override
        public Statement visitExpressionStatementSemicolon(JavaParser.ExpressionStatementSemicolonContext ctx) {
            return visit(ctx.getChild(0));
        }

        //*** METHOD CALL ***//
        @Override
        public MethodCall visitCall(JavaParser.CallContext ctx) {
            String methodName = textVisitor.visit(ctx.callName());
            List<Expression> arguments;
            arguments = ctx.callArguments() == null ? Collections.emptyList() : visit(ctx.callArguments());
            return new MethodCall(JavaGrammarHelper.getOriginalText(ctx), context.getMethodName(), methodName,
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
            context.addVariable(new Variable(variableDef));
            return variableDef;
        }

        @Override
        public Statement visitFieldDec(JavaParser.FieldDecContext ctx) {
            // NOT FULLY SUPPORTED YET
            return visit(ctx.varDec());
        }

        // TODO save method args in context
        @Override
        public Statement visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = textVisitor.visit(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id, EmptyExpressionPreparer.prepare(type));
        }

        @Override
        public Statement visitVarDec(JavaParser.VarDecContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            String type = textVisitor.visit(ctx.type());
            Expression expression;
            if (ctx.expression() != null) {
                expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            } else {
                expression = ctx.parent instanceof JavaParser.FieldDecContext
                        ? EmptyExpressionPreparer.prepare(type)
                        : EmptyExpressionPreparer.prepareUninitialized(id);
            }
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id, expression);
        }

        //*** ASSIGNMENT ***//
        @Override
        public Statement visitAssignment(JavaParser.AssignmentContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            Expression expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            context.updateVariable(id, expression);
            return new Assignment(JavaGrammarHelper.getOriginalText(ctx), id, expression);
        }

        //*** IF ***//
        @Override
        public Statement visitIfElseStatement(JavaParser.IfElseStatementContext ctx) {
            return IfStmtResolver.resolve(context, this, ctx);
        }

        //*** SWITCH ***//
        @Override
        public Statement visitSwitchStatement(JavaParser.SwitchStatementContext ctx) {
            return SwitchStmtResolver.resolve(context.createLocalContext(), ctx);
        }

        //*** BREAK ***//
        @Override
        public Statement visitBreakStatement(JavaParser.BreakStatementContext ctx) {
            throw new BreakStatementException();
        }

        //*** FOR ***//
        @Override
        public Statement visitForStatement(JavaParser.ForStatementContext ctx) {
            JavaContext localContext = context.createLocalContext();
            return ForStmtResolver.resolve(localContext, ctx);
        }

        //*** WHILE ***//
        @Override
        public Statement visitWhileStatement(JavaParser.WhileStatementContext ctx) {
            JavaContext localContext = context.createLocalContext();
            return WhileStmtResolver.resolve(localContext, ctx);
        }

        //*** DO WHILE ***//
        @Override
        public Statement visitDoWhileStatement(JavaParser.DoWhileStatementContext ctx) {
            JavaContext localContext = context.createLocalContext();
            return DoWhileStmtResolver.resolve(localContext, ctx);
        }

        //*** EMPTY ***//
        @Override
        public Statement visitEmptyStatement(JavaParser.EmptyStatementContext ctx) {
            return EmptyStatement.INSTANCE;
        }
    }
}