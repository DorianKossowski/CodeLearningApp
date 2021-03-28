package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.SwitchStatement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.constant.ConstantProvider;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.PrimitiveValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;
import com.server.parser.util.exception.ResolvingException;

import java.util.*;
import java.util.stream.Collectors;

public class SwitchStmtResolver extends StatementResolver {
    private static final String EXCEPTION_PREFIX = "W instrukcji switch: ";
    private static final String EXCEPTION_SUFFIX = " nie jest jednego z typów: char, byte, short, int, Character, " +
            "Byte, Short, Integer, String";
    private static Integer defaultIndex;

    public static SwitchStatement resolve(JavaContext context, JavaParser.SwitchStatementContext switchCtx) {
        defaultIndex = null;
        JavaParser.ExpressionContext expressionContext = switchCtx.expression();
        Value value = resolveExpression(context, expressionContext);
        List<SwitchElement> switchElements = switchCtx.switchElement().stream()
                .map(switchElementContext -> SwitchStmtResolver.resolveSwitchElement(context, switchElementContext))
                .collect(Collectors.toList());
        List<JavaParser.StatementListContext> switchStatementContexts = switchElements.stream()
                .map(SwitchElement::getStatementListContext)
                .collect(Collectors.toList());
        validateStatementLists(context, switchStatementContexts);
        List<List<Expression>> switchLabels = switchElements.stream()
                .map(SwitchElement::getLabelExpressions)
                .collect(Collectors.toList());
        validateLabels(switchLabels);
        List<Statement> contentStatements = resolveStatements(context, value, switchElements);
        contentStatements.forEach(statement ->
                addProperty(statement, StatementProperties.SWITCH_EXPRESSION, expressionContext.getText())
        );
        return new SwitchStatement(contentStatements);
    }

    private static void addProperty(Statement statement, String propertyName, String value) {
        statement.getExpressionStatements()
                .forEach(exprStatement -> exprStatement.addProperty(propertyName, value));
    }

    private static List<Statement> resolveStatements(JavaContext context, Value value, List<SwitchElement> switchElements) {
        Integer fulfilledCaseIndex = null;
        for (int i = 0; i < switchElements.size(); ++i) {
            if (isLabelFulfilled(value, switchElements.get(i))) {
                fulfilledCaseIndex = i;
                break;
            }
        }
        if (fulfilledCaseIndex != null) {
            return resolveStatements(context, switchElements, fulfilledCaseIndex);
        } else if (defaultIndex != null) {
            return resolveStatements(context, switchElements, defaultIndex);
        }
        return Collections.emptyList();
    }

    static List<Statement> resolveStatements(JavaContext context, List<SwitchElement> switchElements, int startIndex) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (int i = startIndex; i < switchElements.size(); ++i) {
            SwitchElement switchElement = switchElements.get(i);
            List<Statement> visitedStmts = context.resolveStatements(context, switchElement.getStatementListContext());
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

    private static String getElementJoinedLabels(SwitchElement switchElement) {
        return switchElement.getLabelExpressions().stream()
                .map(expression -> expression == null ? "default" : expression.getText())
                .collect(Collectors.joining(","));
    }

    static boolean isLabelFulfilled(Value value, SwitchElement switchElement) {
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

    static void validateLabels(List<List<Expression>> switchLabels) {
        LinkedHashSet<String> distinctLabelExpressionTexts = new LinkedHashSet<>();
        List<String> labelExpressionTexts = new ArrayList<>();
        for (int i = 0; i < switchLabels.size(); ++i) {
            validateLabel(distinctLabelExpressionTexts, labelExpressionTexts, switchLabels.get(i), i);
        }
    }

    static private void validateLabel(LinkedHashSet<String> distinctLabelExpressionTexts, List<String> labelExpressionTexts,
                                      List<Expression> switchLabel, int switchLabelIndex) {
        for (Expression labelExpression : switchLabel) {
            if (labelExpression == null) {
                validateDefaultLabel(switchLabelIndex);
            } else {
                validateCaseLabel(distinctLabelExpressionTexts, labelExpressionTexts, labelExpression);
            }
        }
    }

    static private void validateCaseLabel(LinkedHashSet<String> distinctLabelExpressionTexts,
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

    private static void validateDefaultLabel(int switchLabelIndex) {
        if (defaultIndex == null) {
            defaultIndex = switchLabelIndex;
        } else {
            throw new ResolvingException("Zduplikowana etykieta default w instrukcji switch");
        }
    }

    static SwitchElement resolveSwitchElement(JavaContext context,
                                              JavaParser.SwitchElementContext switchElementContext) {
        List<Expression> labelExpressions = resolveLabelExpressions(context, switchElementContext.switchElementLabel());
        JavaParser.StatementListContext statementListContext = switchElementContext.statementList();
        return new SwitchElement(labelExpressions, statementListContext);
    }

    static void validateStatementLists(JavaContext context, List<JavaParser.StatementListContext> statementListContexts) {
        JavaContext validationContext = ContextFactory.createValidationContext(context);
        statementListContexts.forEach(
                statementListContext -> validationContext.resolveStatements(validationContext, statementListContext)
        );
    }

    static List<Expression> resolveLabelExpressions(JavaContext context,
                                                    List<JavaParser.SwitchElementLabelContext> switchElementLabelContexts) {
        return switchElementLabelContexts.stream().map(switchElementLabelContext -> {
            if (switchElementLabelContext.CASE() != null) {
                return context.resolveExpression(context, switchElementLabelContext.expression());
            }
            return null;
        }).collect(Collectors.toList());
    }

    static Value resolveExpression(JavaContext context, JavaParser.ExpressionContext expressionContext) {
        Expression expression = context.resolveExpression(context, expressionContext);
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
        private final JavaParser.StatementListContext statementListContext;

        SwitchElement(List<Expression> labelExpressions, JavaParser.StatementListContext statementListContext) {
            this.labelExpressions = Objects.requireNonNull(labelExpressions, "labelExpressions cannot be null");
            this.statementListContext = Objects.requireNonNull(statementListContext, "statementListContext cannot be null");
        }

        public List<Expression> getLabelExpressions() {
            return labelExpressions;
        }

        public JavaParser.StatementListContext getStatementListContext() {
            return statementListContext;
        }
    }
}