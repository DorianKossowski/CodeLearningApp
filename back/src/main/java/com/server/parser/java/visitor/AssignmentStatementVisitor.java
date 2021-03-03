package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.ObjectRef;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class AssignmentStatementVisitor extends JavaVisitor<Assignment> {

    @Override
    public Assignment visit(ParserRuleContext ctx, JavaContext context) {
        return new AssignmentStatementVisitorInternal(context).visit(ctx);
    }

    private static class AssignmentStatementVisitorInternal extends JavaBaseVisitor<Assignment> {
        private final JavaContext context;

        public AssignmentStatementVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public Assignment visitAssignment(JavaParser.AssignmentContext ctx) {
            String assignmentId;
            Expression expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
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
            ObjectRef objectRef = context.getVisitor(ObjectRef.class)
                    .visit(ctx.assignmentAttributeIdentifier().objectRefName(), context);
            String attribute = textVisitor.visit(ctx.assignmentAttributeIdentifier().attribute);
            objectRef.getValue().updateAttribute(attribute, expression);
        }
    }
}