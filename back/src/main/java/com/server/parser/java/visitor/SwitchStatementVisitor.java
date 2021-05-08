package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.SwitchStatement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.ConstantProvider;
import com.server.parser.java.value.PrimitiveValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;
import com.server.parser.util.exception.ResolvingException;

import java.util.*;
import java.util.stream.Collectors;

public class SwitchStatementVisitor extends JavaVisitor<SwitchStatement> {
    private static final String EXCEPTION_PREFIX = "W instrukcji switch: ";
    private static final String EXCEPTION_SUFFIX = " nie jest jednego z typów: char, byte, short, int, Character, " +
            "Byte, Short, Integer, String";
    private Integer defaultIndex = null;

    private final JavaContext context;

    SwitchStatementVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public SwitchStatement visitSwitchStatement(JavaParser.SwitchStatementContext ctx) {
        JavaParser.ExpressionContext expressionContext = ctx.expression();
        Value value = resolveExpression(expressionContext);
        List<SwitchElement> switchElements = ctx.switchElement().stream()
                .map(this::resolveSwitchElement)
                .collect(Collectors.toList());
        validateSwitch(switchElements);
        List<Statement> contentStatements = resolveStatements(value, switchElements);
        contentStatements.forEach(statement ->
                addProperty(statement, StatementProperties.SWITCH_EXPRESSION, expressionContext.getText())
        );
        return new SwitchStatement(contentStatements);
    }

    private void validateSwitch(List<SwitchElement> switchElements) {
        List<JavaParser.StatementsContext> switchStatementContexts = switchElements.stream()
                .map(SwitchElement::getStatementsContext)
                .collect(Collectors.toList());
        validateStatementLists(switchStatementContexts);

        List<List<Expression>> switchLabels = switchElements.stream()
                .map(SwitchElement::getLabelExpressions)
                .collect(Collectors.toList());
        validateLabels(switchLabels);
    }

    private void addProperty(Statement statement, String propertyName, String value) {
        statement.getExpressionStatements()
                .forEach(exprStatement -> exprStatement.addProperty(propertyName, value));
    }

    private List<Statement> resolveStatements(Value value, List<SwitchElement> switchElements) {
        Integer fulfilledCaseIndex = null;
        for (int i = 0; i < switchElements.size(); ++i) {
            if (isLabelFulfilled(value, switchElements.get(i))) {
                fulfilledCaseIndex = i;
                break;
            }
        }
        if (fulfilledCaseIndex != null) {
            return resolveStatements(switchElements, fulfilledCaseIndex);
        } else if (defaultIndex != null) {
            return resolveStatements(switchElements, defaultIndex);
        }
        return Collections.emptyList();
    }

    List<Statement> resolveStatements(List<SwitchElement> switchElements, int startIndex) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (int i = startIndex; i < switchElements.size(); ++i) {
            SwitchElement switchElement = switchElements.get(i);
            List<Statement> visitedStmts = context.resolveStatements(switchElement.getStatementsContext()).getStatements();
            for (Statement visitedStmt : visitedStmts) {
                addProperty(visitedStmt, StatementProperties.SWITCH_LABELS, getElementJoinedLabels(switchElement));
                statements.add(visitedStmt);
                if (ReturnHandler.shouldReturn(visitedStmt) || BreakHandler.shouldBreak(visitedStmt)) {
                    return statements;
                }
            }
        }
        return statements;
    }

    private String getElementJoinedLabels(SwitchElement switchElement) {
        return switchElement.getLabelExpressions().stream()
                .map(expression -> expression == null ? "default" : expression.getText())
                .collect(Collectors.joining(","));
    }

    boolean isLabelFulfilled(Value value, SwitchElement switchElement) {
        boolean fulfilled;
        for (Expression labelExpression : switchElement.getLabelExpressions()) {
            if (labelExpression == null) {
                continue;
            }
            Value labelValue = labelExpression.getValue();
            if (value instanceof PrimitiveValue) {
                fulfilled = value.equalsOperator(labelValue);
            } else {
                fulfilled = value.equalsMethod(labelValue);
            }
            if (fulfilled) {
                return true;
            }
        }
        return false;
    }

    void validateLabels(List<List<Expression>> switchLabels) {
        LinkedHashSet<String> distinctLabelExpressionTexts = new LinkedHashSet<>();
        List<String> labelExpressionTexts = new ArrayList<>();
        for (int i = 0; i < switchLabels.size(); ++i) {
            validateLabel(distinctLabelExpressionTexts, labelExpressionTexts, switchLabels.get(i), i);
        }
    }

    private void validateLabel(LinkedHashSet<String> distinctLabelExpressionTexts, List<String> labelExpressionTexts,
                               List<Expression> switchLabel, int switchLabelIndex) {
        for (Expression labelExpression : switchLabel) {
            if (labelExpression == null) {
                validateDefaultLabel(switchLabelIndex);
            } else {
                validateCaseLabel(distinctLabelExpressionTexts, labelExpressionTexts, labelExpression);
            }
        }
    }

    private void validateCaseLabel(LinkedHashSet<String> distinctLabelExpressionTexts,
                                   List<String> labelExpressionTexts, Expression labelExpression) {
//        TODO handle const expressions
//        if (!(labelExpression instanceof Literal)) {
//            throw new ResolvingException("Etykieta case wymaga stałego wyrażenia, którym nie jest: " + labelExpression.getText());
//        }
        String resolvedText = labelExpression.getResolvedText();
        if (distinctLabelExpressionTexts.add(resolvedText)) {
            labelExpressionTexts.add(resolvedText);
        } else {
            throw new ResolvingException(String.format("Zduplikowana etykieta %s w instrukcji switch", resolvedText));
        }
    }

    private void validateDefaultLabel(int switchLabelIndex) {
        if (defaultIndex == null) {
            defaultIndex = switchLabelIndex;
        } else {
            throw new ResolvingException("Zduplikowana etykieta default w instrukcji switch");
        }
    }

    SwitchElement resolveSwitchElement(JavaParser.SwitchElementContext switchElementContext) {
        List<Expression> labelExpressions = resolveLabelExpressions(switchElementContext.switchElementLabel());
        JavaParser.StatementsContext statementListContext = switchElementContext.statements();
        return new SwitchElement(labelExpressions, statementListContext);
    }

    void validateStatementLists(List<JavaParser.StatementsContext> statementListContexts) {
        JavaContext validationContext = ContextFactory.createValidationContext(context);
        statementListContexts.forEach(validationContext::resolveStatements);
    }

    List<Expression> resolveLabelExpressions(List<JavaParser.SwitchElementLabelContext> switchElementLabelContexts) {
        return switchElementLabelContexts.stream().map(switchElementLabelContext -> {
            if (switchElementLabelContext.CASE() != null) {
                return context.resolveExpression(switchElementLabelContext.expression());
            }
            return null;
        }).collect(Collectors.toList());
    }

    Value resolveExpression(JavaParser.ExpressionContext expressionContext) {
        Expression expression = context.resolveExpression(expressionContext);
        Value value = expression.getValue();
        if (!(value instanceof ConstantProvider)) {
            throw new ResolvingException(EXCEPTION_PREFIX + value.getExpression().getText() + EXCEPTION_SUFFIX);
        }
        Class<?> valueClass = ((ConstantProvider) value).getConstant().c.getClass();
        if (valueClass.equals(Character.class) || valueClass.equals(Integer.class) || valueClass.equals(String.class)) {
            return value;
        }
        throw new ResolvingException(EXCEPTION_PREFIX + value.getExpression().getText() + EXCEPTION_SUFFIX);
    }

    static class SwitchElement {
        private final List<Expression> labelExpressions;
        private final JavaParser.StatementsContext statementsContext;

        SwitchElement(List<Expression> labelExpressions, JavaParser.StatementsContext statementsContext) {
            this.labelExpressions = Objects.requireNonNull(labelExpressions, "labelExpressions cannot be null");
            this.statementsContext = Objects.requireNonNull(statementsContext, "statementsContext cannot be null");
        }

        public List<Expression> getLabelExpressions() {
            return labelExpressions;
        }

        public JavaParser.StatementsContext getStatementsContext() {
            return statementsContext;
        }
    }
}