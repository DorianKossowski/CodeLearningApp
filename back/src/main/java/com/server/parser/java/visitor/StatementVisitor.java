package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.ObjectRef;
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
import com.server.parser.util.TypeCorrectnessChecker;
import com.server.parser.util.exception.BreakStatementException;
import com.server.parser.util.exception.InvalidReturnedExpressionException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Objects;

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
            return context.getVisitor(VariableDef.class).visit(ctx, context);
        }

        //*** ASSIGNMENT ***//
        @Override
        public Statement visitAssignment(JavaParser.AssignmentContext ctx) {
            String assignmentId;
            Expression expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            if (ctx.assignmentAttributeIdentifier() == null) {
                assignmentId = textVisitor.visit(ctx.identifier());
                context.updateVariable(assignmentId, expression);
            } else {
                assignmentId = textVisitor.visit(ctx.assignmentAttributeIdentifier());
                ObjectRef objectRef = context.getVisitor(ObjectRef.class)
                        .visit(ctx.assignmentAttributeIdentifier().objectRefName(), context);
                String attribute = textVisitor.visit(ctx.assignmentAttributeIdentifier().attribute);
                objectRef.getValue().updateAttribute(attribute, expression);
            }
            return new Assignment(JavaGrammarHelper.getOriginalText(ctx), assignmentId, expression);
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