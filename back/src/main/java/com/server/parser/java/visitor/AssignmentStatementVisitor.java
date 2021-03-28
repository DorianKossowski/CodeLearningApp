package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class AssignmentStatementVisitor extends JavaVisitor<Assignment> {
    private final JavaContext context;

    public AssignmentStatementVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public Assignment visit(ParserRuleContext ctx, JavaContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Assignment visitAssignment(JavaParser.AssignmentContext ctx) {
        String assignmentId;
        Expression expression = context.getVisitor(Expression.class, context).visit(ctx.expression());
        if (ctx.assignmentAttributeIdentifier() == null) {
            assignmentId = textVisitor.visit(ctx.identifier());
            context.updateVariable(assignmentId, expression);
        } else {
            assignmentId = textVisitor.visit(ctx.assignmentAttributeIdentifier());
            updateAttribute(ctx, expression);
        }
        return new Assignment(JavaGrammarHelper.getOriginalText(ctx), assignmentId, expression);
    }

    private void updateAttribute(JavaParser.AssignmentContext ctx, Expression expression) {
        ObjectRefExpression objectRef = context.getVisitor(ObjectRefExpression.class, context)
                .visit(ctx.assignmentAttributeIdentifier().objectRefName());
        String attribute = textVisitor.visit(ctx.assignmentAttributeIdentifier().attribute);
        objectRef.getValue().updateAttribute(attribute, expression);
    }
}