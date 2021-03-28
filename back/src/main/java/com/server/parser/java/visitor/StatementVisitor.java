package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.ast.statement.expression_statement.BreakExprStatement;
import com.server.parser.java.ast.statement.expression_statement.ReturnExprStatement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ContextParameters;
import com.server.parser.java.context.DelegatingContext;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.DoWhileStmtResolver;
import com.server.parser.java.visitor.resolver.ForStmtResolver;
import com.server.parser.java.visitor.resolver.SwitchStmtResolver;
import com.server.parser.java.visitor.resolver.WhileStmtResolver;
import com.server.parser.util.TypeCorrectnessChecker;
import com.server.parser.util.exception.BreakStatementException;
import com.server.parser.util.exception.InvalidReturnedExpressionException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Objects;

public class StatementVisitor extends JavaVisitor<Statement> {
    private final DelegatingContext context;

    StatementVisitor(JavaContext context) {
        this.context = (DelegatingContext) Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public Statement visitBlockStatement(JavaParser.BlockStatementContext ctx) {
        JavaContext localContext = context.createLocalContext();
        List<Statement> statements = localContext.resolveStatements(ctx.statementList());
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
    public CallStatement visitCallStatement(JavaParser.CallStatementContext ctx) {
        return context.getVisitor(CallStatement.class, context).visit(ctx);
    }

    //*** VARIABLE ***//
    @Override
    public Statement visitMethodVarDec(JavaParser.MethodVarDecContext ctx) {
        return context.getVisitor(VariableDef.class, context).visit(ctx);
    }

    @Override
    public Statement visitAssignment(JavaParser.AssignmentContext ctx) {
        return context.getVisitor(Assignment.class, context).visit(ctx);
    }

    //*** IF ***//
    @Override
    public Statement visitIfElseStatement(JavaParser.IfElseStatementContext ctx) {
        return context.getVisitor(IfElseStatement.class, context).visit(ctx);
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
            expression = context.resolveExpression(ctx.expression());
        }
        ContextParameters parameters = context.getParameters();
        if (TypeCorrectnessChecker.isCorrect(parameters.getMethodResultType(), expression)) {
            return new ReturnExprStatement(JavaGrammarHelper.getOriginalText(ctx), expression);
        }
        throw new InvalidReturnedExpressionException(expression.getResolvedText(), parameters.getMethodResultType());
    }
}