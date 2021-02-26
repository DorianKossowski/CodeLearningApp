package com.server.parser.java.call.executor;

import com.rits.cloning.Cloner;
import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.FieldVarInitExpressionSupplier;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.ClonerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConstructorCallExecutor extends CallExecutor {
    private final Cloner cloner;

    public ConstructorCallExecutor() {
        this(new StatementListVisitor(), ClonerFactory.createCloner(FieldVarInitExpressionSupplier.class));
    }

    ConstructorCallExecutor(StatementListVisitor visitor, Cloner cloner) {
        super(visitor);
        this.cloner = Objects.requireNonNull(cloner, "cloner cannot be null");
    }

    @Override
    public CallStatement execute(Method method, CallInvocation invocation) {
        Instance instance = prepareNewInstance(method);
        JavaContext executionContext = ContextFactory.createExecutionContext(instance, method.getMethodContext());
        List<Statement> statements = executeInContext(method, invocation, executionContext);
        return new CallStatement(invocation, statements, instance);
    }

    Instance prepareNewInstance(Method method) {
        Map<String, FieldVar> instanceFields = getInstanceFields(method.getMethodContext().getFields());
        Instance instance = new Instance(method.getClassName(), instanceFields);
        instance.getFields().values().forEach(FieldVar::initialize);
        return instance;
    }

    Map<String, FieldVar> getInstanceFields(Map<String, FieldVar> nameToField) {
        Map<String, FieldVar> instanceFields = new HashMap<>();
        nameToField.forEach((name, fieldVar) -> instanceFields.put(name, getInstanceField(fieldVar)));
        return instanceFields;
    }

    private FieldVar getInstanceField(FieldVar fieldVar) {
        if (fieldVar.isStatic()) {
            return fieldVar;
        }
        return cloner.deepClone(fieldVar);
    }
}