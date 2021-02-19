package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.BlockStatement;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.EmptyStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.ast.statement.expression_statement.BreakExprStatement;
import com.server.parser.java.ast.statement.expression_statement.ReturnExprStatement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.*;
import com.server.parser.util.EmptyExpressionPreparer;
import com.server.parser.util.TypeCorrectnessChecker;
import com.server.parser.util.exception.BreakStatementException;
import com.server.parser.util.exception.InvalidReturnedExpressionException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatementVisitor extends JavaVisitor<Statement> {

    @Override
    public Statement visit(ParserRuleContext ctx, JavaContext context) {
        return new StatementVisitorInternal(context).visit(ctx);
    }

    public static class StatementVisitorInternal extends JavaBaseVisitor<Statement> {
        private final JavaContext context;

        private StatementVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public Statement visitBlockStatement(JavaParser.BlockStatementContext ctx) {
            StatementListVisitor statementListVisitor = new StatementListVisitor();
            List<Statement> statements = statementListVisitor.visit(ctx.statementList(), context.createLocalContext());
            return new BlockStatement(statements);
        }

        @Override
        public Statement visitExpressionStatement(JavaParser.ExpressionStatementContext ctx) {
            return visit(ctx.getChild(0));
        }

        @Override
        public Statement visitExpressionStatementSemicolon(JavaParser.ExpressionStatementSemicolonContext ctx) {
            return visit(ctx.getChild(0));
        }

        //*** CALL ***//
        @Override
        public CallStatement visitCall(JavaParser.CallContext ctx) {
            return context.getVisitor(CallStatement.class).visit(ctx, context);
        }

        //*** VARIABLE ***//
        @Override
        public Statement visitMethodVarDec(JavaParser.MethodVarDecContext ctx) {
            VariableDef variableDef = (VariableDef) visit(ctx.varDec());
            List<String> modifiers = ctx.varModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            variableDef.setModifiers(modifiers);
            variableDef.setContextMethodName(context.getMethodName());
            context.addVariable(new Variable(variableDef));
            return variableDef;
        }

        @Override
        public Statement visitFieldDec(JavaParser.FieldDecContext ctx) {
            VariableDef variableDef = (VariableDef) visit(ctx.varDec());
            List<String> modifiers = ctx.fieldModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            variableDef.setModifiers(modifiers);
            context.addField(new Variable(variableDef));
            return variableDef;
        }

        @Override
        public Statement visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = textVisitor.visit(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id,
                    EmptyExpressionPreparer.prepare(type), false);
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
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id, expression, ctx.expression() != null);
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
            if (isValidBreak(ctx)) {
                return BreakExprStatement.INSTANCE;
            }
            throw new BreakStatementException();
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

        //*** RETURN ***//
        @Override
        public Statement visitReturnStatement(JavaParser.ReturnStatementContext ctx) {
            Expression expression = VoidExpression.INSTANCE;
            if (ctx.expression() != null) {
                expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            }
            if (TypeCorrectnessChecker.isCorrect(context.getMethodResultType(), expression)) {
                return new ReturnExprStatement(JavaGrammarHelper.getOriginalText(ctx), expression);
            }
            throw new InvalidReturnedExpressionException(expression.getResolvedText(), context.getMethodResultType());
        }
    }
}