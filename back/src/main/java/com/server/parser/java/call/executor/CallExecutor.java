package com.server.parser.java.call.executor;

import com.google.common.collect.Streams;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.ReturnExprStatement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.TypeCorrectnessChecker;
import com.server.parser.util.exception.InvalidReturnedExpressionException;
import com.server.parser.util.exception.ResolvingException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CallExecutor {
    static final int MAX_EXECUTION_LEVEL = 10;
    private int executionLevel = 0;

    public abstract CallStatement execute(Method method, CallInvocation invocation);

    List<Statement> executeInContext(Method method, CallInvocation invocation, JavaContext executionContext) {
        preExecution();
        assignInvocationParameters(method.getHeader().getArguments(), invocation.getArgs(), executionContext);
        JavaParser.MethodBodyContext bodyContext = method.getBodyContext();
        List<Statement> statements = bodyContext != null ?
                executionContext.resolveStatements(bodyContext).getStatements() :
                Collections.emptyList();
        postExecution();
        return statements;
    }

    void preExecution() {
        if (executionLevel++ > MAX_EXECUTION_LEVEL) {
            throw new ResolvingException("Przekroczono ilość dopuszczalnych zagnieżdżonych wywołań równą: " + MAX_EXECUTION_LEVEL);
        }
    }

    private void postExecution() {
        --executionLevel;
    }

    Expression getReturnedExpression(List<Statement> statements) {
        Optional<ReturnExprStatement> optionalReturnStatement = statements.stream()
                .filter(statement -> !(statement instanceof CallStatement))
                .flatMap(statement -> statement.getExpressionStatements().stream())
                .filter(statement -> statement instanceof ReturnExprStatement)
                .map(statement -> (ReturnExprStatement) statement)
                .findFirst();
        return optionalReturnStatement.map(ReturnExprStatement::getExpression)
                .orElse(VoidExpression.INSTANCE);
    }

    void validateReturnedExpression(String resultTypeName, Expression returnedExpression) {
        if (!resultTypeName.equals("void") && returnedExpression instanceof VoidExpression) {
            throw new ResolvingException("Brak odpowiedniej instrukcji zwracającej");
        }
        if (!TypeCorrectnessChecker.isCorrect(resultTypeName, returnedExpression)) {
            throw new InvalidReturnedExpressionException(returnedExpression.getResolvedText(), resultTypeName);
        }
    }

    void assignInvocationParameters(List<VariableDef> arguments, List<Expression> invocationParameters, JavaContext executionContext) {
        List<String> argumentsNames = arguments.stream()
                .map(VariableDef::getName)
                .collect(Collectors.toList());
        Streams.forEachPair(argumentsNames.stream(), invocationParameters.stream(), executionContext::updateVariable);
    }
}